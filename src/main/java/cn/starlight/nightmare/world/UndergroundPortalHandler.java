package cn.starlight.nightmare.world;

import cn.starlight.nightmare.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class UndergroundPortalHandler {
    private static final int PORTAL_SEARCH_RADIUS = 32;
    private static final int EXIT_SEARCH_RADIUS = 16;

    public static boolean tryCreatePortal(Level level, BlockPos ignitionPos) {
        if (!level.dimension().equals(Level.OVERWORLD) && !UndergroundWorld.isUnderground(level)) return false;

        PortalFrame frame = findEmptyPortalFrame(level, ignitionPos);
        if (frame == null) return false;

        fillPortal(level, frame, ModBlocks.UNDERGROUND_PORTAL);
        return true;
    }

    public static Optional<BlockPos> findClosestPortal(ServerLevel level, BlockPos origin, Block portalBlock) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        BlockPos closest = null;
        double closestDistance = Double.MAX_VALUE;
        for (int x = origin.getX() - PORTAL_SEARCH_RADIUS; x <= origin.getX() + PORTAL_SEARCH_RADIUS; x++) {
            for (int z = origin.getZ() - PORTAL_SEARCH_RADIUS; z <= origin.getZ() + PORTAL_SEARCH_RADIUS; z++) {
                for (int y = level.getMinY(); y < level.getMaxY(); y++) {
                    cursor.set(x, y, z);
                    if (!level.getBlockState(cursor).is(portalBlock)) continue;

                    double distance = cursor.distSqr(origin);
                    if (distance < closestDistance) {
                        closest = cursor.immutable();
                        closestDistance = distance;
                    }
                }
            }
        }
        return Optional.ofNullable(closest);
    }

    public static boolean isValidPortalFrame(LevelReader level, BlockPos pos, Direction.Axis axis, Block portalBlock) {
        Direction widthDirection = widthDirection(axis);
        BlockPos bottomLeft = pos;
        int depth = 0;
        while (depth++ < 21 && level.getBlockState(bottomLeft.below()).is(portalBlock)) {
            bottomLeft = bottomLeft.below();
        }
        if (!level.getBlockState(bottomLeft.below()).is(Blocks.CRYING_OBSIDIAN)) return false;

        int left = 0;
        while (left++ < 21 && level.getBlockState(bottomLeft.relative(widthDirection.getOpposite())).is(portalBlock)) {
            bottomLeft = bottomLeft.relative(widthDirection.getOpposite());
        }
        if (!level.getBlockState(bottomLeft.relative(widthDirection.getOpposite())).is(Blocks.CRYING_OBSIDIAN)) return false;

        int width = portalWidth(level, bottomLeft, widthDirection, portalBlock);
        if (width < 2 || width > 21 || !level.getBlockState(bottomLeft.relative(widthDirection, width)).is(Blocks.CRYING_OBSIDIAN)) return false;

        return hasCompleteFrame(level, new PortalFrame(bottomLeft, axis, width, portalHeight(level, bottomLeft, widthDirection, width, portalBlock)), portalBlock);
    }

    public static BlockPos createReturnPortal(ServerLevel level, BlockPos desiredPos, Direction.Axis axis, Block portalBlock) {
        return new UndergroundPortalForcer(level, portalBlock).createPortal(desiredPos, axis)
                .map(rectangle -> rectangle.minCorner)
                .orElse(null);
    }

    private static PortalFrame findExitFrame(ServerLevel level, BlockPos desiredPos, Direction.Axis axis, int y, boolean findFallback) {
        if (y < level.getMinY() + 1 || y + 3 >= level.getMaxY()) return null;

        PortalFrame fallback = null;
        for (int radius = 0; radius <= EXIT_SEARCH_RADIUS; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (radius != 0 && Math.abs(x) != radius && Math.abs(z) != radius) continue;

                    PortalFrame frame = new PortalFrame(new BlockPos(desiredPos.getX() + x, y, desiredPos.getZ() + z), axis, 2, 3);
                    if (!isInsideWorldBorder(level, frame)) continue;
                    if (hasClearPortalVolume(level, frame)) return frame;
                    if (findFallback && fallback == null) fallback = frame;
                }
            }
        }
        return fallback;
    }

    private static PortalFrame findEmptyPortalFrame(Level level, BlockPos pos) {
        PortalFrame frame = findEmptyPortalFrame(level, pos, Direction.Axis.X);
        return frame != null ? frame : findEmptyPortalFrame(level, pos, Direction.Axis.Z);
    }

    private static PortalFrame findEmptyPortalFrame(Level level, BlockPos pos, Direction.Axis axis) {
        Direction widthDirection = widthDirection(axis);
        BlockPos bottomLeft = pos;
        while (bottomLeft.getY() > level.getMinY() && isPortalSpace(level.getBlockState(bottomLeft.below()))) {
            bottomLeft = bottomLeft.below();
        }
        if (!level.getBlockState(bottomLeft.below()).is(Blocks.CRYING_OBSIDIAN)) return null;

        int left = 0;
        while (left++ < 21 && isPortalSpace(level.getBlockState(bottomLeft.relative(widthDirection.getOpposite())))) {
            bottomLeft = bottomLeft.relative(widthDirection.getOpposite());
        }
        if (!level.getBlockState(bottomLeft.relative(widthDirection.getOpposite())).is(Blocks.CRYING_OBSIDIAN)) return null;

        int width = emptyPortalWidth(level, bottomLeft, widthDirection);
        if (width < 2 || width > 21 || !level.getBlockState(bottomLeft.relative(widthDirection, width)).is(Blocks.CRYING_OBSIDIAN)) return null;

        PortalFrame frame = new PortalFrame(bottomLeft, axis, width, emptyPortalHeight(level, bottomLeft, widthDirection, width));
        return hasCompleteFrame(level, frame, null) ? frame : null;
    }

    private static int emptyPortalWidth(Level level, BlockPos bottomLeft, Direction widthDirection) {
        int width = 0;
        while (width <= 21 && isPortalSpace(level.getBlockState(bottomLeft.relative(widthDirection, width)))) {
            width++;
        }
        return width;
    }

    private static int portalWidth(LevelReader level, BlockPos bottomLeft, Direction widthDirection, Block portalBlock) {
        int width = 0;
        while (width <= 21 && level.getBlockState(bottomLeft.relative(widthDirection, width)).is(portalBlock)) {
            width++;
        }
        return width;
    }

    private static int emptyPortalHeight(Level level, BlockPos bottomLeft, Direction widthDirection, int width) {
        int height = 0;
        while (height <= 21 && isPortalRow(level, bottomLeft.above(height), widthDirection, width, null)) {
            height++;
        }
        return height;
    }

    private static int portalHeight(LevelReader level, BlockPos bottomLeft, Direction widthDirection, int width, Block portalBlock) {
        int height = 0;
        while (height <= 21 && isPortalRow(level, bottomLeft.above(height), widthDirection, width, portalBlock)) {
            height++;
        }
        return height;
    }

    private static boolean hasCompleteFrame(LevelReader level, PortalFrame frame, Block portalBlock) {
        if (frame.height() < 3 || frame.height() > 21) return false;

        Direction widthDirection = widthDirection(frame.axis());
        for (int width = 0; width < frame.width(); width++) {
            if (!level.getBlockState(frame.bottomLeft().relative(widthDirection, width).below()).is(Blocks.CRYING_OBSIDIAN)) return false;
        }

        for (int height = 0; height < frame.height(); height++) {
            BlockPos row = frame.bottomLeft().above(height);
            if (!isPortalRow(level, row, widthDirection, frame.width(), portalBlock)) return false;
            if (!level.getBlockState(row.relative(widthDirection.getOpposite())).is(Blocks.CRYING_OBSIDIAN)
                    || !level.getBlockState(row.relative(widthDirection, frame.width())).is(Blocks.CRYING_OBSIDIAN)) return false;
        }

        BlockPos top = frame.bottomLeft().above(frame.height());
        for (int width = 0; width < frame.width(); width++) {
            if (!level.getBlockState(top.relative(widthDirection, width)).is(Blocks.CRYING_OBSIDIAN)) return false;
        }
        return true;
    }

    private static boolean isPortalRow(LevelReader level, BlockPos pos, Direction widthDirection, int width, Block portalBlock) {
        for (int index = 0; index < width; index++) {
            BlockState state = level.getBlockState(pos.relative(widthDirection, index));
            if (portalBlock == null ? !isPortalSpace(state) : !state.is(portalBlock)) return false;
        }
        return true;
    }

    private static boolean isInsideWorldBorder(ServerLevel level, PortalFrame frame) {
        Direction widthDirection = widthDirection(frame.axis());
        for (int width = -1; width <= frame.width(); width++) {
            if (!level.getWorldBorder().isWithinBounds(frame.bottomLeft().relative(widthDirection, width))) return false;
        }
        return true;
    }

    private static boolean hasClearPortalVolume(ServerLevel level, PortalFrame frame) {
        Direction widthDirection = widthDirection(frame.axis());
        for (int height = 0; height <= frame.height(); height++) {
            for (int width = -1; width <= frame.width(); width++) {
                if (!level.getBlockState(frame.bottomLeft().relative(widthDirection, width).above(height)).isAir()) return false;
            }
        }

        Direction depthDirection = frame.axis() == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        for (int height = 0; height < frame.height(); height++) {
            for (int width = 0; width < frame.width(); width++) {
                BlockPos portalPos = frame.bottomLeft().relative(widthDirection, width).above(height);
                if (!level.getBlockState(portalPos.relative(depthDirection)).isAir()
                        || !level.getBlockState(portalPos.relative(depthDirection.getOpposite())).isAir()) return false;
            }
        }
        return true;
    }

    private static void clearPortalVolume(ServerLevel level, PortalFrame frame) {
        Direction widthDirection = widthDirection(frame.axis());
        for (int height = 0; height <= frame.height(); height++) {
            for (int width = -1; width <= frame.width(); width++) {
                level.setBlock(frame.bottomLeft().relative(widthDirection, width).above(height), Blocks.AIR.defaultBlockState(), 18);
            }
        }

        Direction depthDirection = frame.axis() == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        for (int height = 0; height < frame.height(); height++) {
            for (int width = 0; width < frame.width(); width++) {
                BlockPos portalPos = frame.bottomLeft().relative(widthDirection, width).above(height);
                level.setBlock(portalPos.relative(depthDirection), Blocks.AIR.defaultBlockState(), 18);
                level.setBlock(portalPos.relative(depthDirection.getOpposite()), Blocks.AIR.defaultBlockState(), 18);
            }
        }
    }

    private static void fillPortal(Level level, PortalFrame frame, Block portalBlock) {
        BlockState state = portalBlock.defaultBlockState().setValue(NetherPortalBlock.AXIS, frame.axis());
        Direction widthDirection = widthDirection(frame.axis());
        for (int height = 0; height < frame.height(); height++) {
            for (int width = 0; width < frame.width(); width++) {
                level.setBlock(frame.bottomLeft().relative(widthDirection, width).above(height), state, 18);
            }
        }
    }

    private static void fillGeneratedPortal(ServerLevel level, PortalFrame frame, Block portalBlock) {
        Direction widthDirection = widthDirection(frame.axis());
        for (int width = -1; width <= frame.width(); width++) {
            level.setBlock(frame.bottomLeft().relative(widthDirection, width).below(), Blocks.CRYING_OBSIDIAN.defaultBlockState(), 2);
            level.setBlock(frame.bottomLeft().relative(widthDirection, width).above(frame.height()), Blocks.CRYING_OBSIDIAN.defaultBlockState(), 2);
        }
        for (int height = 0; height < frame.height(); height++) {
            BlockPos row = frame.bottomLeft().above(height);
            level.setBlock(row.relative(widthDirection.getOpposite()), Blocks.CRYING_OBSIDIAN.defaultBlockState(), 2);
            level.setBlock(row.relative(widthDirection, frame.width()), Blocks.CRYING_OBSIDIAN.defaultBlockState(), 2);
        }
        fillPortal(level, frame, portalBlock);
    }

    private static boolean isPortalSpace(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE);
    }

    private static Direction widthDirection(Direction.Axis axis) {
        return axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
    }

    private record PortalFrame(BlockPos bottomLeft, Direction.Axis axis, int width, int height) {
    }
}
