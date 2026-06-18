package cn.starlight.nightmare.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class NightmareConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("nightmare-client.json");
    private static NightmareConfig instance;

    public boolean showGeneralTooltips = true;
    public boolean showNutritionTooltips = true;

    public static NightmareConfig get() {
        if (instance == null) load();
        return instance;
    }

    public static void load() {
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                instance = GSON.fromJson(reader, NightmareConfig.class);
            } catch (IOException ignored) {
            }
        }
        if (instance == null) instance = new NightmareConfig();
        save();
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(get(), writer);
        } catch (IOException ignored) {
        }
    }
}
