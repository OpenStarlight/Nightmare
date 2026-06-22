package cn.starlight.nightmare.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.BlockUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

public class UndergroundPortalForcer {
    private final ServerLevel level;
    private final Block portalBlock;

    public UndergroundPortalForcer(ServerLevel level, Block portalBlock) {
        this.level = level;
        this.portalBlock = portalBlock;
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double bestDistance = -1.0D;
        BlockPos bestPos = null;
        double fallbackDistance = -1.0D;
        BlockPos fallbackPos = null;
        WorldBorder border = level.getWorldBorder();
        int maxY = Math.min(level.getMaxY(), level.getMinY() + level.getLogicalHeight() - 1);
        BlockPos.MutableBlockPos mutable = pos.mutable();

        for (BlockPos.MutableBlockPos candidate : BlockPos.spiralAround(pos, 16, Direction.EAST, Direction.SOUTH)) {
            int height = Math.min(maxY, level.getHeight(Heightmap.Types.MOTION_BLOCKING, candidate.getX(), candidate.getZ()));
            if (!border.isWithinBounds(candidate) || !border.isWithinBounds(candidate.move(direction, 1))) continue;
            candidate.move(direction.getOpposite(), 1);

            for (int y = height; y >= level.getMinY(); y--) {
                candidate.setY(y);
                if (!canPortalReplaceBlock(candidate)) continue;

                int floorY = y;
                while (floorY > level.getMinY() && canPortalReplaceBlock(candidate.move(Direction.DOWN))) {
                    floorY--;
                }
                if (floorY + 4 > maxY) continue;

                int drop = y - floorY;
                if (drop > 0 && drop < 3) continue;

                candidate.setY(floorY);
                if (!canHostFrame(candidate, mutable, direction, 0)) continue;

                double distance = pos.distSqr(candidate);
                if (canHostFrame(candidate, mutable, direction, -1) && canHostFrame(candidate, mutable, direction, 1)
                        && (bestDistance == -1.0D || bestDistance > distance)) {
                    bestDistance = distance;
                    bestPos = candidate.immutable();
                }
                if (bestDistance == -1.0D && (fallbackDistance == -1.0D || fallbackDistance > distance)) {
                    fallbackDistance = distance;
                    fallbackPos = candidate.immutable();
                }
            }
        }

        if (bestDistance == -1.0D && fallbackDistance != -1.0D) {
            bestDistance = fallbackDistance;
            bestPos = fallbackPos;
        }

        if (bestDistance == -1.0D) {
            int minY = Math.max(level.getMinY() - 1, 70);
            int maximumFallbackY = maxY - 9;
            if (maximumFallbackY < minY) return Optional.empty();

            bestPos = border.clampToBounds(new BlockPos(pos.getX() - direction.getStepX(), Mth.clamp(pos.getY(), minY, maximumFallbackY), pos.getZ() - direction.getStepZ()));
            Direction clockwise = direction.getClockWise();
            for (int offset = -1; offset < 2; offset++) {
                for (int width = 0; width < 2; width++) {
                    for (int height = -1; height < 3; height++) {
                        BlockState state = height < 0 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                        mutable.setWithOffset(bestPos, width * direction.getStepX() + offset * clockwise.getStepX(), height,
                                width * direction.getStepZ() + offset * clockwise.getStepZ());
                        level.setBlockAndUpdate(mutable, state);
                    }
                }
            }
        }

        for (int width = -1; width < 3; width++) {
            for (int height = -1; height < 4; height++) {
                if (width == -1 || width == 2 || height == -1 || height == 3) {
                    mutable.setWithOffset(bestPos, width * direction.getStepX(), height, width * direction.getStepZ());
                    level.setBlock(mutable, Blocks.CRYING_OBSIDIAN.defaultBlockState(), 3);
                }
            }
        }

        BlockState portalState = portalBlock.defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);
        for (int width = 0; width < 2; width++) {
            for (int height = 0; height < 3; height++) {
                mutable.setWithOffset(bestPos, width * direction.getStepX(), height, width * direction.getStepZ());
                level.setBlock(mutable, portalState, 18);
            }
        }
        return Optional.of(new BlockUtil.FoundRectangle(bestPos.immutable(), 2, 3));
    }

    private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.canBeReplaced() && state.getFluidState().isEmpty();
    }

    private boolean canHostFrame(BlockPos origin, BlockPos.MutableBlockPos mutable, Direction direction, int offset) {
        Direction clockwise = direction.getClockWise();
        for (int width = -1; width < 3; width++) {
            for (int height = -1; height < 4; height++) {
                mutable.setWithOffset(origin, direction.getStepX() * width + clockwise.getStepX() * offset, height,
                        direction.getStepZ() * width + clockwise.getStepZ() * offset);
                if (height < 0 ? !level.getBlockState(mutable).isSolid() : !canPortalReplaceBlock(mutable)) return false;
            }
        }
        return true;
    }
}
