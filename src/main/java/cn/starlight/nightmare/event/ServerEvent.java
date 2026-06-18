package cn.starlight.nightmare.event;

import cn.starlight.nightmare.modifier.ItemModifier;
import cn.starlight.nightmare.player.CapabilitySystem;
import cn.starlight.nightmare.player.NutritionSystem;
import cn.starlight.nightmare.util.player.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.Set;

public class ServerEvent {

    // 设置玩家属性
    public void tickPlayer(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            CapabilitySystem.handleHealth(player);
            CapabilitySystem.handleFood(player);
            CapabilitySystem.handleAir(player);
            CapabilitySystem.handleInteractionRange(player);
            CapabilitySystem.handleLevelBonuses(player);
            NutritionSystem.tickPlayer(player);
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

    // 死亡后继承20%经验
    public void restoreDeathXp(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        if (alive) return;
        if (oldPlayer.level().getGameRules().get(GameRules.KEEP_INVENTORY)) return;
        int total = (int)(5 * oldPlayer.experienceLevel * (oldPlayer.experienceLevel + 1)
                + oldPlayer.experienceProgress * 10 * (oldPlayer.experienceLevel + 1));
        newPlayer.giveExperiencePoints((int)(total * 0.2f));
    }
}
