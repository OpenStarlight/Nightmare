package cn.starlight.nightmare.util.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public class StringUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();

    public static Component toComponent(String string) {
        JsonElement json = JsonParser.parseString(GSON_SERIALIZER.serialize(MINI_MESSAGE.deserialize(string)));
        return ComponentSerialization.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }
}
