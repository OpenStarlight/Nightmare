package cn.starlight.nightmare.event;

import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.world.UndergroundWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UndergroundWorldEvents {
    private static final Map<UUID, Integer> HEAT_TICKS = new HashMap<>();

    public static void tickHeat(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            boolean underground = UndergroundWorld.isUnderground(level);
            boolean nether = level.dimension().equals(Level.NETHER);
            if (!underground && !nether) continue;

            for (ServerPlayer player : level.players()) {
                boolean nearHeat = false;
                BlockPos origin = player.blockPosition();
                for (int x = -2; x <= 2 && !nearHeat; x++) {
                    for (int y = -2; y <= 2 && !nearHeat; y++) {
                        for (int z = -2; z <= 2; z++) {
                            if (x * x + y * y + z * z >= 9) continue;
                            if (underground ? level.getBlockState(origin.offset(x, y, z)).is(ModBlocks.MANTLE) : level.getBlockState(origin.offset(x, y, z)).is(ModBlocks.CORE)) {
                                nearHeat = true;
                                break;
                            }
                        }
                    }
                }

                if (!nearHeat) {
                    HEAT_TICKS.remove(player.getUUID());
                    continue;
                }

                int ticks = HEAT_TICKS.merge(player.getUUID(), 1, Integer::sum);
                if (ticks >= 40 && ticks % 20 == 0) player.igniteForSeconds(4.0F);
            }
        }
    }
}
