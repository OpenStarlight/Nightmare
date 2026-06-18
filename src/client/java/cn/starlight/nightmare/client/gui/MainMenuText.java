package cn.starlight.nightmare.client.gui;

import cn.starlight.nightmare.NightmareMod;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.TitleScreen;

public class MainMenuText {
    private static final String TEXT = "Nightmare " + FabricLoader.getInstance().getModContainer(NightmareMod.MOD_ID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("unknown");

    public static void initialize() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen) {
                ScreenEvents.afterExtract(screen).register((screen1, graphics, mouseX, mouseY, tickProgress) ->
                        graphics.text(client.font, TEXT, 2, graphics.guiHeight() - 20, 0xFFFFFFFF));
            }
        });
    }
}
