package cn.starlight.nightmare;

import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.event.ServerEvent;
import cn.starlight.nightmare.modifier.BlockModifier;
import cn.starlight.nightmare.modifier.ItemModifier;
import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.registry.ModAttributes;
import cn.starlight.nightmare.util.DebugFields;
import cn.starlight.nightmare.util.player.PlayerUtil;
import cn.starlight.nightmare.world.ModWorldGeneration;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NightmareMod implements ModInitializer {
    public static final String MOD_ID = "nightmare";
    public static final Logger logger = LoggerFactory.getLogger("Nightmare");
    public static final boolean debug = true;

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        ModItems.initialize();
        ModAttributes.initialize();
        ItemModifier.initialize();
        BlockModifier.initialize();
        ModWorldGeneration.initialize();

        ServerEvent serverEvent = new ServerEvent();
        ServerTickEvents.END_SERVER_TICK.register(serverEvent::tickPlayerStats);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> serverEvent.removeForbiddenItems(handler.player));

        if (debug) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatcher.register(Commands.literal("nightmare")
                        .then(Commands.literal("scanOre").then(Commands.argument("radius", IntegerArgumentType.integer(1, 16))
                                .executes(context -> scanOre(context.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(context, "radius")))))
                        .then(Commands.literal("field")
                                .then(Commands.literal("disableBreakSpeedModifier").then(Commands.argument("state", BoolArgumentType.bool())
                                        .executes(context -> {
                                            DebugFields.disableBreakSpeedModifier = BoolArgumentType.getBool(context, "state");
                                            PlayerUtil.showMessage(context.getSource().getPlayerOrException(), "Success", true);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                                .then(Commands.literal("disableToolRequirement").then(Commands.argument("state", BoolArgumentType.bool())
                                        .executes(context -> {
                                            DebugFields.disableToolRequirement = BoolArgumentType.getBool(context, "state");
                                            PlayerUtil.showMessage(context.getSource().getPlayerOrException(), "Success", true);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        ));
            });
        }

        logger.info("Nightmare mod initialized successfully!");
    }

    private static int scanOre(ServerPlayer player, int radius) {
        ServerLevel level = player.level();
        ChunkPos center = player.chunkPosition();
        int[] counts = new int[18];
        int skippedChunks = 0;

        for (int chunkX = center.x() - radius + 1; chunkX <= center.x() + radius - 1; chunkX++) {
            for (int chunkZ = center.z() - radius + 1; chunkZ <= center.z() + radius - 1; chunkZ++) {
                ChunkAccess chunk = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                if (chunk == null) {
                    skippedChunks++;
                    continue;
                }
                for (int x = chunk.getPos().getMinBlockX(); x <= chunk.getPos().getMaxBlockX(); x++) {
                    for (int z = chunk.getPos().getMinBlockZ(); z <= chunk.getPos().getMaxBlockZ(); z++) {
                        for (int y = level.getMinY(); y < level.getMaxY(); y++) {
                            Block block = level.getBlockState(new BlockPos(x, y, z)).getBlock();
                            if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE) counts[0]++;
                            else if (block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE) counts[1]++;
                            else if (block == ModBlocks.SILVER_ORE || block == ModBlocks.DEEPSLATE_SILVER_ORE) counts[2]++;
                            else if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE) counts[3]++;
                            else if (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE) counts[4]++;
                            else if (block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE) counts[5]++;
                            else if (block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE) counts[6]++;
                            else if (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE) counts[7]++;
                            else if (block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE) counts[8]++;
                            else if (block == ModBlocks.MITHRIL_ORE || block == ModBlocks.DEEPSLATE_MITHRIL_ORE) counts[9]++;
                            else if (block == ModBlocks.ADAMANTIUM_ORE) counts[10]++;
                            else if (block == Blocks.NETHER_QUARTZ_ORE) counts[11]++;
                            else if (block == Blocks.NETHER_GOLD_ORE) counts[12]++;
                            else if (block == Blocks.ANCIENT_DEBRIS) counts[13]++;
                            else if (block == Blocks.RAW_IRON_BLOCK) counts[14]++;
                            else if (block == Blocks.RAW_COPPER_BLOCK) counts[15]++;
                            else if (block == Blocks.TUFF) counts[16]++;
                            else if (block == Blocks.GRANITE) counts[17]++;
                        }
                    }
                }
            }
        }

        PlayerUtil.showMessage(player, "Ore scan radius " + radius + " chunks", true);
        PlayerUtil.showMessage(player, "煤 " + counts[0] + "，铜 " + counts[1] + "，银 " + counts[2] + "，铁 " + counts[3], false);
        PlayerUtil.showMessage(player, "金 " + counts[4] + "，红石 " + counts[5] + "，青金石 " + counts[6] + "，钻石 " + counts[7] + "，绿宝石 " + counts[8], false);
        PlayerUtil.showMessage(player, "秘银 " + counts[9] + "，艾德曼 " + counts[10] + "，下界石英 " + counts[11] + "，下界金 " + counts[12] + "，远古残骸 " + counts[13], false);
        PlayerUtil.showMessage(player, "矿脉粗铁块 " + counts[14] + "，矿脉粗铜块 " + counts[15] + "，凝灰岩 " + counts[16] + "，花岗岩 " + counts[17] + (skippedChunks > 0 ? "，跳过未加载区块 " + skippedChunks : ""), false);
        return Command.SINGLE_SUCCESS;
    }
}
