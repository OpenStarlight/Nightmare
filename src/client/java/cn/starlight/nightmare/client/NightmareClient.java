package cn.starlight.nightmare.client;

import cn.starlight.nightmare.client.config.NightmareConfig;
import cn.starlight.nightmare.client.gui.MainMenuText;
import cn.starlight.nightmare.client.item.ItemTooltips;
import net.fabricmc.api.ClientModInitializer;

public class NightmareClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NightmareConfig.load();
        ItemTooltips.initialize();
        MainMenuText.initialize();
    }
}
