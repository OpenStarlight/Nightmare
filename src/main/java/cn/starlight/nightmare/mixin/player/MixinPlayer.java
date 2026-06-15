package cn.starlight.nightmare.mixin.player;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Shadow
    public int experienceLevel;

    // 总是可以吃东西
    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    private void alwaysCanEat(boolean canAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    /**
     * @author Stars
     * @reason 使用自定义经验曲线
     */
    @Overwrite
    public int getXpNeededForNextLevel() {
        return 10 * (this.experienceLevel + 1);
    }
}
