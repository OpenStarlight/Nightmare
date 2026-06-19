package cn.starlight.nightmare.player.crafting;

import net.minecraft.world.entity.player.Player;

public interface CraftingProgressMenu {
    boolean nightmare$startCraftingProgress(Player player, boolean quickMove);

    boolean nightmare$startCraftingSwapProgress(Player player, int hotbarSlot);

    void nightmare$tickCraftingProgress();

    void nightmare$cancelCraftingProgress();

    boolean nightmare$isCraftingInProgress();

    boolean nightmare$hasTimedCraftingResult();

    int nightmare$getCraftingProgress();

    int nightmare$getCraftingTotalTime();

    int nightmare$getCraftingCycle();

    void nightmare$applyCraftingProgress(int progress, int totalTime, int cycle);
}
