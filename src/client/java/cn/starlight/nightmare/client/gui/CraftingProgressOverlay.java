package cn.starlight.nightmare.client.gui;

import cn.starlight.nightmare.player.crafting.CraftingProgressMenu;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.WeakHashMap;

public class CraftingProgressOverlay {
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/burn_progress");
    private static final Identifier INVENTORY_PROGRESS_SPRITE = Identifier.fromNamespaceAndPath("nightmare", "crafting/inventory_progress");
    private static final Map<CraftingProgressMenu, ProgressState> PROGRESS_STATES = new WeakHashMap<>();

    public static void draw(GuiGraphicsExtractor graphics, CraftingProgressMenu menu, int x, int y, int width, int height) {
        draw(graphics, menu, BURN_PROGRESS_SPRITE, x, y, width, height);
    }

    public static void drawInventory(GuiGraphicsExtractor graphics, CraftingProgressMenu menu, int x, int y) {
        draw(graphics, menu, INVENTORY_PROGRESS_SPRITE, x, y, 16, 13);
    }

    private static void draw(GuiGraphicsExtractor graphics, CraftingProgressMenu menu, Identifier sprite, int x, int y, int width, int height) {
        int total = menu.nightmare$getCraftingTotalTime();
        if (total <= 0) {
            PROGRESS_STATES.remove(menu);
            return;
        }

        int progress = currentProgress(menu, total);
        int progressWidth = progress <= 0 ? 0 : Math.max(1, progress * width / total);
        if (progressWidth > 0) {
            graphics.enableScissor(x, y, x + progressWidth, y + height);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, width, height);
            graphics.disableScissor();
        }
    }

    private static int currentProgress(CraftingProgressMenu menu, int total) {
        long now = System.currentTimeMillis();
        int cycle = menu.nightmare$getCraftingCycle();
        int serverProgress = Math.min(menu.nightmare$getCraftingProgress(), total);
        ProgressState state = PROGRESS_STATES.computeIfAbsent(menu, ignored -> new ProgressState(cycle, total, serverProgress, now));

        if (state.cycle != cycle || state.total != total || serverProgress < state.progress) {
            state.cycle = cycle;
            state.total = total;
            state.progress = serverProgress;
            state.lastTickTime = now;
        } else if (serverProgress > state.progress) {
            state.progress = serverProgress;
            state.lastTickTime = now;
        } else if (state.progress < total && now - state.lastTickTime >= 50L) {
            int elapsedTicks = (int) ((now - state.lastTickTime) / 50L);
            state.progress = Math.min(total, state.progress + elapsedTicks);
            state.lastTickTime += elapsedTicks * 50L;
        }

        return state.progress;
    }

    private static class ProgressState {
        private int cycle;
        private int total;
        private int progress;
        private long lastTickTime;

        private ProgressState(int cycle, int total, int progress, long lastTickTime) {
            this.cycle = cycle;
            this.total = total;
            this.progress = progress;
            this.lastTickTime = lastTickTime;
        }
    }
}
