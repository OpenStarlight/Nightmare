package cn.starlight.nightmare.event;

import cn.starlight.nightmare.NightmareMod;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class WorldProgressEvent {
    private static final SavedDataType<Data> DATA_TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "world_progress"),
            Data::new,
            Data.CODEC,
            DataFixTypes.SAVED_DATA_COMMAND_STORAGE
    );
    private static volatile boolean hasIronIngot;
    private static volatile MinecraftServer loadedServer;

    public static void load(MinecraftServer server) {
        hasIronIngot = data(server).hasIronIngot;
        loadedServer = server;
    }

    public static void reset(MinecraftServer server) {
        if (loadedServer != server) return;
        hasIronIngot = false;
        loadedServer = null;
    }

    public static boolean hasIronIngot() {
        return hasIronIngot;
    }

    public static void checkIronIngot(ServerPlayer player) {
        MinecraftServer server = player.level().getServer();
        if (loadedServer != server) load(server);
        if (hasIronIngot) return;
        if (!player.getInventory().contains(new ItemStack(Items.IRON_INGOT))) return;

        Data data = data(server);
        if (!data.hasIronIngot) {
            data.hasIronIngot = true;
            data.setDirty();
        }
        hasIronIngot = true;
        loadedServer = server;
    }

    private static Data data(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(DATA_TYPE);
    }

    public static class Data extends SavedData {
        public static final Codec<Data> CODEC = Codec.BOOL.fieldOf("has_iron_ingot").codec().xmap(Data::new, data -> data.hasIronIngot);
        private boolean hasIronIngot;

        public Data() {
            this(false);
        }

        private Data(boolean hasIronIngot) {
            this.hasIronIngot = hasIronIngot;
        }
    }
}
