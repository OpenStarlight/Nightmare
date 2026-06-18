package cn.starlight.nightmare.client.integration;

import cn.starlight.nightmare.client.config.NightmareConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class NightmareModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return NightmareConfigScreen::create;
    }
}
