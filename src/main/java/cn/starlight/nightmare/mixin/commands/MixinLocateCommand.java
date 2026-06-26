package cn.starlight.nightmare.mixin.commands;

import cn.starlight.nightmare.config.NightmareConfig;
import cn.starlight.nightmare.event.WorldProgressEvent;
import cn.starlight.nightmare.util.world.AsyncStructureSearch;
import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LocateCommand.class)
public class MixinLocateCommand {
    @Inject(method = "locateStructure", at = @At("HEAD"), cancellable = true)
    private static void nightmare$locateStructureAsync(CommandSourceStack source, ResourceOrTagKeyArgument.Result<Structure> structure,
                                                       CallbackInfoReturnable<Integer> cir) {
        if (!NightmareConfig.get().server.asyncStructureSearch) return;

        ServerLevel level = source.getLevel();
        Registry<Structure> registry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        Optional<? extends HolderSet.ListBacked<Structure>> optionalHolders = structure.unwrap().map(
                key -> registry.get(key).map(holder -> HolderSet.direct(holder)),
                registry::get
        );
        if (optionalHolders.isEmpty()) {
            source.sendFailure(Component.translatable("commands.locate.structure.invalid", structure.asPrintable()));
            cir.setReturnValue(0);
            return;
        }

        HolderSet<Structure> holders = optionalHolders.get();
        if (level.dimension().equals(Level.OVERWORLD) && !WorldProgressEvent.hasIronIngot() && holders.stream().anyMatch(MixinLocateCommand::nightmare$isVillageStructure)) {
            source.sendFailure(Component.translatable("message.nightmare.command.locateVillageLocked"));
            cir.setReturnValue(0);
            return;
        }

        BlockPos origin = BlockPos.containing(source.getPosition());
        Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
        source.sendSystemMessage(Component.translatable("message.nightmare.command.locateStructureAsyncStarted", structure.asPrintable()));

        AsyncStructureSearch
                .submit(() -> level.getChunkSource().getGenerator().findNearestMapStructure(level, holders, origin, 100, false))
                .thenAccept(searchResult -> source.getServer().execute(() -> {
                    stopwatch.stop();
                    if (searchResult.status() == AsyncStructureSearch.Status.BUSY) {
                        source.sendFailure(Component.translatable("message.nightmare.command.locateStructureAsyncBusy"));
                        return;
                    }
                    if (searchResult.status() == AsyncStructureSearch.Status.FAILED) {
                        source.sendFailure(Component.translatable("message.nightmare.command.locateStructureAsyncFailed", structure.asPrintable()));
                        return;
                    }
                    Pair<BlockPos, ? extends net.minecraft.core.Holder<Structure>> result = searchResult.value();
                    if (result == null) {
                        source.sendFailure(Component.translatable("commands.locate.structure.not_found", structure.asPrintable()));
                        return;
                    }
                    LocateCommand.showLocateResult(source, structure, origin, result, "commands.locate.structure.success", false, stopwatch.elapsed());
                }));

        cir.setReturnValue(1);
    }

    private static boolean nightmare$isVillageStructure(Holder<Structure> holder) {
        return holder.unwrapKey().map(key -> {
            Identifier id = key.identifier();
            return id.getNamespace().equals("minecraft") && id.getPath().startsWith("village_");
        }).orElse(false);
    }
}
