package cn.starlight.nightmare;

import cn.starlight.nightmare.block.ModBlocks;
import cn.starlight.nightmare.player.crafting.CraftingProgressTimes;
import cn.starlight.nightmare.event.ServerEvent;
import cn.starlight.nightmare.item.ModItems;
import cn.starlight.nightmare.modifier.BlockModifier;
import cn.starlight.nightmare.modifier.ItemModifier;
import cn.starlight.nightmare.network.CraftingProgressPayload;
import cn.starlight.nightmare.player.NutritionSystem;
import cn.starlight.nightmare.player.effect.ModEffects;
import cn.starlight.nightmare.util.DebugFields;
import cn.starlight.nightmare.util.player.PlayerUtil;
import cn.starlight.nightmare.world.ModWorldGeneration;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.List;
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
        CraftingProgressTimes.initialize();
        PayloadTypeRegistry.clientboundPlay().register(CraftingProgressPayload.TYPE, CraftingProgressPayload.CODEC);
        ModWorldGeneration.initialize();
        ModEffects.initialize();

        ItemModifier.initialize();
        BlockModifier.initialize();

        ServerEvent serverEvent = new ServerEvent();
        ServerTickEvents.END_SERVER_TICK.register(serverEvent::tickPlayer);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> serverEvent.removeForbiddenItems(handler.player));
        ServerPlayerEvents.COPY_FROM.register(serverEvent::restoreDeathXp);

        if (debug) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatcher.register(Commands.literal("nightmare").requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                        .then(Commands.literal("get")
                                .then(Commands.literal("itemTags")
                                        .executes(context -> getItemTags(context.getSource().getPlayerOrException())))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.literal("protein")
                                                .executes(context -> getNutrition(context.getSource().getPlayerOrException(), EntityArgument.getPlayer(context, "player"), NutritionSystem.PROTEIN)))
                                        .then(Commands.literal("phytonutrient")
                                                .executes(context -> getNutrition(context.getSource().getPlayerOrException(), EntityArgument.getPlayer(context, "player"), NutritionSystem.PHYTONUTRIENT)))
                                        .then(Commands.literal("insulinResistance")
                                                .executes(context -> getNutrition(context.getSource().getPlayerOrException(), EntityArgument.getPlayer(context, "player"), NutritionSystem.INSULIN_RESISTANCE)))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.literal("protein")
                                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                                        .executes(context -> setNutrition(context.getSource().getPlayerOrException(), EntityArgument.getPlayer(context, "player"), NutritionSystem.PROTEIN, DoubleArgumentType.getDouble(context, "value")))))
                                        .then(Commands.literal("phytonutrient")
                                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                                        .executes(context -> setNutrition(context.getSource().getPlayerOrException(), EntityArgument.getPlayer(context, "player"), NutritionSystem.PHYTONUTRIENT, DoubleArgumentType.getDouble(context, "value")))))
                                        .then(Commands.literal("insulinResistance")
                                                .then(Commands.argument("value", DoubleArgumentType.doubleArg())
                                                        .executes(context -> setNutrition(context.getSource().getPlayerOrException(), EntityArgument.getPlayer(context, "player"), NutritionSystem.INSULIN_RESISTANCE, DoubleArgumentType.getDouble(context, "value")))))))
                        .then(Commands.literal("field")
                                .then(Commands.literal("disableBreakSpeedModifier").then(Commands.argument("state", BoolArgumentType.bool())
                                        .executes(context -> {
                                            DebugFields.disableBreakSpeedModifier = BoolArgumentType.getBool(context, "state");
                                            PlayerUtil.showMessage(context.getSource().getPlayerOrException(), "<lang:message.nightmare.command.fieldSet:'[disableBreakSpeedModifier]'>", true);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                                .then(Commands.literal("disableToolRequirement").then(Commands.argument("state", BoolArgumentType.bool())
                                        .executes(context -> {
                                            DebugFields.disableToolRequirement = BoolArgumentType.getBool(context, "state");
                                            PlayerUtil.showMessage(context.getSource().getPlayerOrException(), "<lang:message.nightmare.command.fieldSet:'[disableToolRequirement]'>", true);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        ));
            });
        }

        logger.info("Nightmare mod initialized successfully!");
    }

    private static int getNutrition(ServerPlayer source, ServerPlayer target, Holder<Attribute> attribute) {
        AttributeInstance instance = target.getAttribute(attribute);
        if (instance == null) return 0;
        PlayerUtil.showMessage(source, "<lang:message.nightmare.command.attributeGet:'" + target.getName().getString() + "':'" + attribute.getRegisteredName() + "':'<aqua>" + (int)instance.getBaseValue() + "'>", true);
        return Command.SINGLE_SUCCESS;
    }

    private static int getItemTags(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            player.sendSystemMessage(Component.literal("Hold an item in your main hand."));
            return 0;
        }

        List<String> tags = stack.typeHolder().tags()
                .map(TagKey::location)
                .map(id -> "#" + id)
                .sorted()
                .toList();
        if (tags.isEmpty()) {
            player.sendSystemMessage(Component.literal(stack.getHoverName().getString() + " has no item tags."));
            return Command.SINGLE_SUCCESS;
        }

        player.sendSystemMessage(Component.literal(stack.getHoverName().getString() + " item tags: " + String.join(", ", tags)));
        return Command.SINGLE_SUCCESS;
    }

    private static int setNutrition(ServerPlayer source, ServerPlayer target, Holder<Attribute> attribute, double value) {
        AttributeInstance instance = target.getAttribute(attribute);
        if (instance == null) return 0;
        instance.setBaseValue(instance.getAttribute().value().sanitizeValue(value));
        PlayerUtil.showMessage(source, "<lang:message.nightmare.command.attributeSet:'" + target.getName().getString() + "':'" + attribute.getRegisteredName() + "':'<aqua>" + (int)instance.getBaseValue() + "'>", true);
        return Command.SINGLE_SUCCESS;
    }
}
