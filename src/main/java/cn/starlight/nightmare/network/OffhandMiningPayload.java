package cn.starlight.nightmare.network;

import cn.starlight.nightmare.NightmareMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record OffhandMiningPayload(boolean active) implements CustomPacketPayload {
    public static final Type<OffhandMiningPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(NightmareMod.MOD_ID, "offhand_mining"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OffhandMiningPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            OffhandMiningPayload::active,
            OffhandMiningPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
