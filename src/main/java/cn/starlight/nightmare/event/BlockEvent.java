package cn.starlight.nightmare.event;

import cn.starlight.nightmare.mixin.block.AccessorFallingBlock;
import cn.starlight.nightmare.util.world.BlockFallingRule;
import cn.starlight.nightmare.util.world.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class BlockEvent {
    private static final int CONDITIONAL_FALLING_MAX_DISTANCE = 8;
    private static final Map<ServerLevel, Set<Long>> PENDING_FALLING_CHECKS = new WeakHashMap<>();

    public static void tickFallingBlocks(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            Set<Long> pending = PENDING_FALLING_CHECKS.remove(level);
            if (pending == null || pending.isEmpty()) continue;

            for (long packedPos : pending) {
                BlockPos pos = BlockPos.of(packedPos);
                if (!level.isLoaded(pos)) continue;

                BlockState state = level.getBlockState(pos);
                if (!canApplyFallingRule(state)) continue;

                BlockFallingRule rule = BlockUtil.getBlockFallingRule(state);
                if (rule == BlockFallingRule.STABLE) continue;
                if (rule == BlockFallingRule.STRICT_FALLING ? canStrictFall(level, pos) : canConditionalFall(level, pos)) {
                    fallBlock(level, pos, state);
                }
            }
        }
    }

    public static void scheduleFallingCheck(LevelReader level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            PENDING_FALLING_CHECKS.computeIfAbsent(serverLevel, ignored -> new HashSet<>()).add(pos.asLong());
        }
    }

    private static boolean canApplyFallingRule(BlockState state) {
        return !state.isAir() && state.getFluidState().isEmpty();
    }

    private static boolean canStrictFall(ServerLevel level, BlockPos pos) {
        return pos.getY() >= level.getMinY() && FallingBlock.isFree(level.getBlockState(pos.below()));
    }

    private static boolean canConditionalFall(ServerLevel level, BlockPos pos) {
        if (pos.getY() <= level.getMinY()) return false;
        return !hasConditionalSupport(level, pos);
    }

    private static boolean hasConditionalSupport(ServerLevel level, BlockPos start) {
        ArrayDeque<SearchNode> queue = new ArrayDeque<>();
        Set<Long> visited = new HashSet<>();
        queue.addLast(new SearchNode(start, 0));
        visited.add(start.asLong());

        while (!queue.isEmpty()) {
            SearchNode node = queue.removeFirst();
            if (node.distance > CONDITIONAL_FALLING_MAX_DISTANCE) continue;

            for (Direction direction : Direction.values()) {
                BlockPos neighbourPos = node.pos.relative(direction);
                if (!level.isLoaded(neighbourPos)) return true;

                BlockState neighbourState = level.getBlockState(neighbourPos);
                BlockFallingRule neighbourRule = BlockUtil.getBlockFallingRule(neighbourState);
                if (neighbourRule != BlockFallingRule.CONDITIONAL_FALLING && neighbourState.isFaceSturdy(level, neighbourPos, direction.getOpposite())) {
                    return true;
                }

                int nextDistance = direction.getAxis().isVertical() ? 0 : node.distance + 1;
                if (nextDistance > CONDITIONAL_FALLING_MAX_DISTANCE || neighbourRule != BlockFallingRule.CONDITIONAL_FALLING || !canApplyFallingRule(neighbourState)) continue;

                long neighbourPacked = neighbourPos.asLong();
                if (visited.add(neighbourPacked)) queue.addLast(new SearchNode(neighbourPos, nextDistance));
            }
        }

        return false;
    }

    private static void fallBlock(ServerLevel level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        CompoundTag blockData = blockEntity == null ? null : blockEntity.saveWithoutMetadata(level.registryAccess());
        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
        if (blockData != null) fallingBlockEntity.blockData = blockData;
        if (state.getBlock() instanceof FallingBlock fallingBlock) {
            ((AccessorFallingBlock) fallingBlock).nightmare$falling(fallingBlockEntity);
        }
    }

    private record SearchNode(BlockPos pos, int distance) {
    }
}
