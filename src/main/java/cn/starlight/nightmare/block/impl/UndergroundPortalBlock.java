package cn.starlight.nightmare.block.impl;

import cn.starlight.nightmare.world.UndergroundPortalHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class UndergroundPortalBlock extends NetherPortalBlock {
    private final ResourceKey<Level> firstDestination;
    private final ResourceKey<Level> secondDestination;

    public UndergroundPortalBlock(BlockBehaviour.Properties properties, ResourceKey<Level> firstDestination, ResourceKey<Level> secondDestination) {
        super(properties);
        this.firstDestination = firstDestination;
        this.secondDestination = secondDestination;
    }

    @Override
    public TeleportTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        ResourceKey<Level> targetKey = level.dimension().equals(firstDestination) ? secondDestination : firstDestination;
        ServerLevel target = level.getServer().getLevel(targetKey);
        if (target == null) return null;

        double scale = net.minecraft.world.level.dimension.DimensionType.getTeleportationScale(level.dimensionType(), target.dimensionType());
        BlockPos searchPos = target.getWorldBorder().clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);
        Direction.Axis axis = level.getBlockState(pos).getValue(AXIS);
        Optional<BlockPos> existingPortal = UndergroundPortalHandler.findClosestPortal(target, searchPos, this);
        BlockPos exitPos = existingPortal.orElseGet(() -> UndergroundPortalHandler.createReturnPortal(target, searchPos, axis, this));
        if (exitPos == null) return null;

        return new TeleportTransition(target, Vec3.atBottomCenterOf(exitPos).add(0.0D, 0.1D, 0.0D), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(),
                TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction,
                                     BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        Direction.Axis axis = state.getValue(AXIS);
        Direction.Axis neighborAxis = direction.getAxis();
        if ((axis != neighborAxis && neighborAxis.isHorizontal()) || neighborState.is(this)
                || UndergroundPortalHandler.isValidPortalFrame(level, pos, axis, this)) {
            return state;
        }
        return Blocks.AIR.defaultBlockState();
    }
}
