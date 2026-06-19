package cn.starlight.nightmare.player.crafting;

import cn.starlight.nightmare.NightmareMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CraftingProgressTimes {
    private static final String DIRECTORY = "nightmare_crafting_time";
    private static final int DEFAULT_TICKS = 40;
    private static final Map<Identifier, Integer> TIMES = new HashMap<>();
    private static final Map<TagKey<Item>, Integer> ITEM_TAG_TIMES = new LinkedHashMap<>();

    public static void initialize() {
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(
                Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "crafting_progress_times"),
                (ResourceManagerReloadListener) manager -> {
                    Map<Identifier, Integer> times = new HashMap<>();
                    Map<TagKey<Item>, Integer> itemTagTimes = new LinkedHashMap<>();
                    manager.listResources(DIRECTORY, id -> id.getPath().endsWith(".json")).forEach((id, resource) ->
                            loadEntry(id, resource, times, itemTagTimes));
                    TIMES.clear();
                    TIMES.putAll(times);
                    ITEM_TAG_TIMES.clear();
                    ITEM_TAG_TIMES.putAll(itemTagTimes);
                });
    }

    public static int ticks(Identifier recipe, ItemStack result) {
        Integer recipeTime = TIMES.get(recipe);
        if (recipeTime != null) return recipeTime;

        for (Map.Entry<TagKey<Item>, Integer> entry : ITEM_TAG_TIMES.entrySet()) {
            if (result.is(entry.getKey())) return entry.getValue();
        }
        return DEFAULT_TICKS;
    }

    private static void loadEntry(Identifier id, Resource resource, Map<Identifier, Integer> times, Map<TagKey<Item>, Integer> itemTagTimes) {
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
            if (json.has("item_tag")) {
                itemTagTimes.put(parseItemTag(json.get("item_tag").getAsString()), ticks);
            }
            if (json.has("item_tags")) {
                JsonArray itemTags = json.getAsJsonArray("item_tags");
                for (JsonElement itemTag : itemTags) {
                    itemTagTimes.put(parseItemTag(itemTag.getAsString()), ticks);
                }
            }
        } catch (Exception exception) {
            NightmareMod.logger.warn("Failed to load crafting progress time {}", id, exception);
        }
    }

    private static TagKey<Item> parseItemTag(String id) {
        if (id.startsWith("#")) id = id.substring(1);
        return TagKey.create(Registries.ITEM, Identifier.parse(id));
    }
}
