package cn.starlight.nightmare;

import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.event.ServerEvent;
import cn.starlight.nightmare.modifier.BlockModifier;
import cn.starlight.nightmare.modifier.ItemModifier;
import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.util.DebugFields;
import cn.starlight.nightmare.util.player.PlayerUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
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
        ItemModifier.initialize();
        BlockModifier.initialize();

        ServerEvent serverEvent = new ServerEvent();
        ServerTickEvents.END_SERVER_TICK.register(serverEvent::tickPlayerStats);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> serverEvent.removeForbiddenItems(handler.player));

        if (debug) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatcher.register(Commands.literal("nightmare").then(Commands.literal("field")
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
}
