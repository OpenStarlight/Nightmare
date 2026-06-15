package cn.starlight.nightmare.util.player;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerUtil {
    private static final String PREFIX = "§7[§b§lNightmare§7] §r";

    public static void showMessage(ServerPlayer player, String msg, boolean prefix) {
        player.sendSystemMessage(Component.literal(prefix ? PREFIX + msg : msg));
    }

    public static void showGlobalMessage(MinecraftServer server, String msg, boolean prefix) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            showMessage(player, msg, prefix);
        }
    }
}
