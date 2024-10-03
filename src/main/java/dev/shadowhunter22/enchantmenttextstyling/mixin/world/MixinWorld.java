package dev.shadowhunter22.enchantmenttextstyling.mixin.world;

import dev.shadowhunter22.enchantmenttextstyling.api.registry.ModRegistryKeys;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Supplier;

@Debug(export = true)
@Mixin(World.class)
public abstract class MixinWorld {
    @Inject(method = "<init>", at = @At("TAIL"))
    protected void getAndSetRegistryManager(MutableWorldProperties properties, RegistryKey registryRef, DynamicRegistryManager registryManager, RegistryEntry dimensionEntry, Supplier profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, CallbackInfo ci) {
        ModRegistryKeys.Manager.registryManager = Optional.of(registryManager);
    }
}
