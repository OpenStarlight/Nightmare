package cn.starlight.nightmare.mixin.inventory;

import cn.starlight.nightmare.player.crafting.CraftingProgressMenu;
import cn.starlight.nightmare.player.crafting.CraftingProgressTimes;
import cn.starlight.nightmare.network.CraftingProgressPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractCraftingMenu.class)
public abstract class MixinAbstractCraftingMenu extends RecipeBookMenu implements CraftingProgressMenu {
    @Shadow
    @Final
    protected ResultContainer resultSlots;

    @Shadow
    @Final
    protected CraftingContainer craftSlots;

    @Unique
    private Identifier nightmare$currentRecipe;
    @Unique
    private int nightmare$craftingProgress;
    @Unique
    private int nightmare$craftingTotalTime;
    @Unique
    private int nightmare$craftingCycle;
    @Unique
    private boolean nightmare$craftingActive;
    @Unique
    private boolean nightmare$craftingQuickMove;
    @Unique
    private int nightmare$craftingSwapSlot = -1;
    @Unique
    private boolean nightmare$completingCraft;
    @Unique
    private int nightmare$craftingRestartGuard;

    @Shadow
    protected abstract Player owner();

    protected MixinAbstractCraftingMenu(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void nightmare$addCraftingProgressData(MenuType<?> menuType, int containerId, int width, int height, CallbackInfo ci) {
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return nightmare$craftingProgress;
            }

            @Override
            public void set(int value) {
                nightmare$craftingProgress = value;
            }
        });
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return nightmare$craftingTotalTime;
            }

            @Override
            public void set(int value) {
                nightmare$craftingTotalTime = value;
            }
        });
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return nightmare$craftingCycle;
            }

            @Override
            public void set(int value) {
                nightmare$craftingCycle = value;
            }
        });
    }

    @Override
    public boolean nightmare$startCraftingProgress(Player player, boolean quickMove) {
        if (this.nightmare$craftingActive) return true;

        RecipeHolder<?> recipe = this.resultSlots.getRecipeUsed();
        ItemStack result = this.resultSlots.getItem(0);
        if (recipe == null || result.isEmpty()) return false;

        Identifier recipeId = recipe.id().identifier();
        int totalTime = CraftingProgressTimes.ticks(recipeId, result);
        if (totalTime <= 0) return false;

        this.nightmare$currentRecipe = recipeId;
        this.nightmare$craftingProgress = 0;
        this.nightmare$craftingTotalTime = totalTime;
        this.nightmare$craftingCycle++;
        this.nightmare$craftingActive = true;
        this.nightmare$craftingQuickMove = quickMove;
        this.nightmare$craftingSwapSlot = -1;
        this.nightmare$sendCraftingProgress();
        return true;
    }

    @Override
    public boolean nightmare$startCraftingSwapProgress(Player player, int hotbarSlot) {
        if (this.nightmare$craftingActive) return true;
        if (hotbarSlot < 0 || hotbarSlot >= 9) return false;

        RecipeHolder<?> recipe = this.resultSlots.getRecipeUsed();
        ItemStack result = this.resultSlots.getItem(0);
        if (recipe == null || result.isEmpty()) return false;

        Identifier recipeId = recipe.id().identifier();
        int totalTime = CraftingProgressTimes.ticks(recipeId, result);
        if (totalTime <= 0) return false;
        if (!this.nightmare$canHotbarAccept(player, hotbarSlot, result)) return false;

        this.nightmare$currentRecipe = recipeId;
        this.nightmare$craftingProgress = 0;
        this.nightmare$craftingTotalTime = totalTime;
        this.nightmare$craftingCycle++;
        this.nightmare$craftingActive = true;
        this.nightmare$craftingQuickMove = false;
        this.nightmare$craftingSwapSlot = hotbarSlot;
        this.nightmare$sendCraftingProgress();
        return true;
    }

    @Override
    public void nightmare$tickCraftingProgress() {
        if (this.nightmare$completingCraft) return;
        if (!this.nightmare$craftingActive) return;
        if (this.nightmare$craftingRestartGuard > 0) {
            this.nightmare$craftingRestartGuard--;
        }

        RecipeHolder<?> recipe = this.resultSlots.getRecipeUsed();
        if (recipe == null || this.resultSlots.getItem(0).isEmpty()) {
            this.nightmare$clearCraftingProgress();
            return;
        }

        Identifier recipeId = recipe.id().identifier();
        int totalTime = CraftingProgressTimes.ticks(recipeId, this.resultSlots.getItem(0));
        if (totalTime <= 0) {
            this.nightmare$clearCraftingProgress();
            return;
        }

        if (!recipeId.equals(this.nightmare$currentRecipe)) {
            this.nightmare$clearCraftingProgress();
            return;
        }

        this.nightmare$craftingTotalTime = totalTime;
        this.nightmare$continueCraftingProgress(totalTime);
        this.nightmare$sendCraftingProgress();
    }

    @Unique
    private void nightmare$continueCraftingProgress(int totalTime) {
        if (this.nightmare$craftingProgress < totalTime) {
            this.nightmare$craftingProgress++;
        }
        if (this.nightmare$craftingProgress >= totalTime) {
            this.nightmare$completeCraftingProgress();
        }
    }

    @Override
    public void nightmare$cancelCraftingProgress() {
        if (!this.nightmare$completingCraft && this.nightmare$craftingRestartGuard <= 0) {
            this.nightmare$clearCraftingProgress();
        }
    }

    @Override
    public boolean nightmare$isCraftingInProgress() {
        return this.nightmare$craftingActive;
    }

    @Override
    public boolean nightmare$hasTimedCraftingResult() {
        RecipeHolder<?> recipe = this.resultSlots.getRecipeUsed();
        return recipe != null && !this.resultSlots.getItem(0).isEmpty() && CraftingProgressTimes.ticks(recipe.id().identifier(), this.resultSlots.getItem(0)) > 0;
    }

    @Override
    public int nightmare$getCraftingProgress() {
        return this.nightmare$craftingProgress;
    }

    @Override
    public int nightmare$getCraftingTotalTime() {
        return this.nightmare$craftingTotalTime;
    }

    @Override
    public int nightmare$getCraftingCycle() {
        return this.nightmare$craftingCycle;
    }

    @Override
    public void nightmare$applyCraftingProgress(int progress, int totalTime, int cycle) {
        this.nightmare$craftingProgress = progress;
        this.nightmare$craftingTotalTime = totalTime;
        this.nightmare$craftingCycle = cycle;
        this.nightmare$craftingActive = totalTime > 0;
    }

    @Unique
    private void nightmare$clearCraftingProgress() {
        this.nightmare$currentRecipe = null;
        this.nightmare$craftingProgress = 0;
        this.nightmare$craftingTotalTime = 0;
        this.nightmare$craftingActive = false;
        this.nightmare$craftingQuickMove = false;
        this.nightmare$craftingSwapSlot = -1;
        this.nightmare$craftingRestartGuard = 0;
        this.nightmare$sendCraftingProgress();
    }

    @Unique
    private void nightmare$sendCraftingProgress() {
        Player player = this.owner();
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new CraftingProgressPayload(this.containerId, this.nightmare$craftingProgress, this.nightmare$craftingTotalTime, this.nightmare$craftingCycle));
        }
    }

    @Unique
    private boolean nightmare$canCarryResult(ItemStack result) {
        ItemStack carried = this.getCarried();
        if (carried.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(carried, result) && carried.getCount() + result.getCount() <= carried.getMaxStackSize();
    }

    @Unique
    private boolean nightmare$canInventoryAccept(Player player, ItemStack result) {
        Inventory inventory = player.getInventory();
        int remaining = result.getCount();

        for (ItemStack stack : inventory.getNonEquipmentItems()) {
            if (stack.isEmpty()) {
                remaining -= result.getMaxStackSize();
            } else if (ItemStack.isSameItemSameComponents(stack, result)) {
                remaining -= stack.getMaxStackSize() - stack.getCount();
            }
            if (remaining <= 0) return true;
        }

        return false;
    }

    @Unique
    private boolean nightmare$canHotbarAccept(Player player, int hotbarSlot, ItemStack result) {
        ItemStack stack = player.getInventory().getItem(hotbarSlot);
        if (stack.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(stack, result) && stack.getCount() + result.getCount() <= stack.getMaxStackSize();
    }

    @Unique
    private void nightmare$completeCraftingProgress() {
        Player player = this.owner();
        this.nightmare$completingCraft = true;
        this.slotsChanged(this.craftSlots);
        Slot resultSlot = this.getSlot(0);
        ItemStack result = this.resultSlots.getItem(0).copy();
        if (result.isEmpty()) {
            this.nightmare$completingCraft = false;
            this.nightmare$clearCraftingProgress();
            return;
        }

        if (this.nightmare$craftingQuickMove) {
            ItemStack toInsert = result.copy();
            if (!this.nightmare$canInventoryAccept(player, toInsert) || !player.getInventory().add(toInsert)) {
                this.nightmare$completingCraft = false;
                this.nightmare$clearCraftingProgress();
                return;
            }
        } else if (this.nightmare$craftingSwapSlot >= 0) {
            if (!this.nightmare$canHotbarAccept(player, this.nightmare$craftingSwapSlot, result)) {
                this.nightmare$completingCraft = false;
                this.nightmare$clearCraftingProgress();
                return;
            }
            ItemStack stack = player.getInventory().getItem(this.nightmare$craftingSwapSlot);
            if (stack.isEmpty()) {
                player.getInventory().setItem(this.nightmare$craftingSwapSlot, result.copy());
            } else {
                stack.grow(result.getCount());
                player.getInventory().setItem(this.nightmare$craftingSwapSlot, stack);
            }
        } else {
            if (this.nightmare$canCarryResult(result)) {
                ItemStack carried = this.getCarried();
                if (carried.isEmpty()) {
                    this.setCarried(result.copy());
                } else {
                    carried.grow(result.getCount());
                    this.setCarried(carried);
                }
            } else {
                ItemStack toInsert = result.copy();
                if (!this.nightmare$canInventoryAccept(player, toInsert) || !player.getInventory().add(toInsert)) {
                    this.nightmare$completingCraft = false;
                    this.nightmare$clearCraftingProgress();
                    return;
                }
            }
        }

        resultSlot.onTake(player, result);
        this.slotsChanged(this.craftSlots);
        this.nightmare$completingCraft = false;

        if (this.nightmare$craftingQuickMove) {
            RecipeHolder<?> nextRecipe = this.resultSlots.getRecipeUsed();
            ItemStack nextResult = this.resultSlots.getItem(0);
            if (nextRecipe != null && !nextResult.isEmpty() && nextRecipe.id().identifier().equals(this.nightmare$currentRecipe) && this.nightmare$canInventoryAccept(player, nextResult)) {
                int totalTime = CraftingProgressTimes.ticks(nextRecipe.id().identifier(), nextResult);
                if (totalTime <= 0) {
                    this.nightmare$clearCraftingProgress();
                    return;
                }
                this.nightmare$craftingProgress = 0;
                this.nightmare$craftingTotalTime = totalTime;
                this.nightmare$craftingCycle++;
                this.nightmare$craftingActive = true;
                this.nightmare$craftingQuickMove = true;
                this.nightmare$craftingSwapSlot = -1;
                this.nightmare$craftingRestartGuard = 2;
                this.nightmare$sendCraftingProgress();
                return;
            }
        }

        this.nightmare$clearCraftingProgress();
    }
}
