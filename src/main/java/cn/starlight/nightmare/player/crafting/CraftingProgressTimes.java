package cn.starlight.nightmare.player.crafting;

import cn.starlight.nightmare.NightmareMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class CraftingProgressTimes {
    private static final String DIRECTORY = "nightmare_crafting_time";
    private static final Map<Identifier, Integer> TIMES = new HashMap<>();

    public static void initialize() {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(
                Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "crafting_progress_times"),
                (ResourceManagerReloadListener) manager -> {
                    Map<Identifier, Integer> times = new HashMap<>();
                    manager.listResources(DIRECTORY, id -> id.getPath().endsWith(".json")).forEach((id, resource) ->
                            loadEntry(id, resource, times));
                    TIMES.clear();
                    TIMES.putAll(times);
                });
    }

    public static int ticks(Identifier recipe) {
        return TIMES.getOrDefault(recipe, 0);
    }

    private static void loadEntry(Identifier id, Resource resource, Map<Identifier, Integer> times) {
        try (BufferedReader reader = resource.openAsReader()) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            int ticks = json.get("ticks").getAsInt();
            if (ticks <= 0) return;

            if (json.has("recipe")) {
                times.put(Identifier.parse(json.get("recipe").getAsString()), ticks);
            }
            if (json.has("recipes")) {
                JsonArray recipes = json.getAsJsonArray("recipes");
                for (JsonElement recipe : recipes) {
                    times.put(Identifier.parse(recipe.getAsString()), ticks);
                }
            }
        } catch (Exception exception) {
            NightmareMod.logger.warn("Failed to load crafting progress time {}", id, exception);
        }
    }
}
