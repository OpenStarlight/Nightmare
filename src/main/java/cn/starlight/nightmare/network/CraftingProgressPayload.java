package cn.starlight.nightmare.network;

import cn.starlight.nightmare.NightmareMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record CraftingProgressPayload(int containerId, int progress, int totalTime, int cycle) implements CustomPacketPayload {
    public static final Type<CraftingProgressPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "crafting_progress"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingProgressPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CraftingProgressPayload::containerId,
            ByteBufCodecs.VAR_INT,
            CraftingProgressPayload::progress,
            ByteBufCodecs.VAR_INT,
            CraftingProgressPayload::totalTime,
            ByteBufCodecs.VAR_INT,
            CraftingProgressPayload::cycle,
            CraftingProgressPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
