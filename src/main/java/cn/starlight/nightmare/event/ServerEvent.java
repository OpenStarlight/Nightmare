package cn.starlight.nightmare.event;

import cn.starlight.nightmare.system.CapabilitySystem;
import cn.starlight.nightmare.modifier.ItemModifier;
import cn.starlight.nightmare.util.player.PlayerUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ServerEvent {

    // 设置玩家各项数值上限
    public void tickPlayerStats(MinecraftServer server) {
        // 不要频繁更新
        boolean updateEverySecond = server.getTickCount() % 20 == 0;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (updateEverySecond) {
                CapabilitySystem.handleHealth(player);
                CapabilitySystem.handleFood(player);
                CapabilitySystem.handleAir(player);
            }
            CapabilitySystem.handleInteractionRange(player);
            CapabilitySystem.handleLevelBonuses(player);
        }
    }

    // 检查并移除被禁止的物品
    public void removeForbiddenItems(ServerPlayer player) {
        int removedTotal = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (ItemModifier.isForbiddenItem(player.getInventory().getItem(i))) {
                player.getInventory().setItem(i, ItemStack.EMPTY);
                removedTotal++;
            }
        }
        if (removedTotal > 0) PlayerUtil.showMessage(player, "§cRemoved " + removedTotal + " forbidden item(s) in your inventory!", true);
    }
}
