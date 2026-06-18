package cn.starlight.nightmare.client.mixin;

import cn.starlight.nightmare.NightmareMod;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void nightmare$updateTitleAfterRendererInitialized(CallbackInfo ci) {
        ((Minecraft) (Object) this).updateTitle();
    }

    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void nightmare$forceTitle(CallbackInfoReturnable<String> cir) {
        String modVersion = FabricLoader.getInstance().getModContainer(NightmareMod.MOD_ID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
        String mcVersion = SharedConstants.getCurrentVersion().name();
        cir.setReturnValue("Nightmare " + modVersion + " (" + mcVersion + "/" + nightmare$renderEngine() + ")");
    }

    @Unique
    private static String nightmare$renderEngine() {
        GpuDevice device = RenderSystem.tryGetDevice();
        if (device == null) return "Unknown";
        String backendName = device.getDeviceInfo().backendName();
        String normalized = backendName.toLowerCase(java.util.Locale.ROOT);
        if (normalized.contains("vulkan")) return "Vulkan";
        if (normalized.contains("opengl") || normalized.contains("open gl")) return "OpenGL";
        return backendName;
    }
}
