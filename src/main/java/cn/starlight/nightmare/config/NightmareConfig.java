package cn.starlight.nightmare.config;

import cn.starlight.nightmare.NightmareMod;
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
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(NightmareMod.MOD_ID + ".json");
    private static NightmareConfig instance;

    public Client client = new Client();
    public Server server = new Server();

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
        if (instance.client == null) instance.client = new Client();
        if (instance.server == null) instance.server = new Server();
        save();
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(PATH)) {
            GSON.toJson(get(), writer);
        } catch (IOException ignored) {
        }
    }

    public static class Client {
        public boolean showGeneralTooltips = true;
        public boolean showNutritionTooltips = true;
    }

    public static class Server {
        public boolean asyncStructureSearch = true;
    }
}
