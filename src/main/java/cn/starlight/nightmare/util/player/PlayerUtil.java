package cn.starlight.nightmare.util.player;

import cn.starlight.nightmare.util.render.StringUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerUtil {
    private static final String PREFIX = "<gray>[<gradient:#36ffcd:#213fff>Nightmare</gradient>]</gray> ";

    public static void showMessage(ServerPlayer player, String msg, boolean prefix) {
        player.sendSystemMessage(StringUtil.toComponent(prefix ? PREFIX + msg : msg));
    }

    public static void showGlobalMessage(MinecraftServer server, String msg, boolean prefix) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            showMessage(player, msg, prefix);
        }
    }
}
