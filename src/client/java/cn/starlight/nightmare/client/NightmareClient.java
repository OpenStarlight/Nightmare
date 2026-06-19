package cn.starlight.nightmare.client;

import cn.starlight.nightmare.client.config.NightmareConfig;
import cn.starlight.nightmare.client.item.ItemTooltips;
import cn.starlight.nightmare.network.CraftingProgressPayload;
import cn.starlight.nightmare.player.crafting.CraftingProgressMenu;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class NightmareClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NightmareConfig.load();
        ItemTooltips.initialize();
        ClientPlayNetworking.registerGlobalReceiver(CraftingProgressPayload.TYPE, (payload, context) -> {
            if (context.player().containerMenu.containerId == payload.containerId() && context.player().containerMenu instanceof CraftingProgressMenu menu) {
                menu.nightmare$applyCraftingProgress(payload.progress(), payload.totalTime(), payload.cycle());
            }
        });
    }
}
