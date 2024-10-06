//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextstyling.registry;

import java.util.Optional;
import java.util.function.Supplier;

import dev.shadowhunter22.enchantmenttextstyling.api.EnchantmentStyling;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.MutableWorldProperties;

import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ModRegistryKeys {
	public static final RegistryKey<Registry<EnchantmentStyling>> STYLING = RegistryKey.ofRegistry(Identifier.of("styling", "enchantment"));

	@ApiStatus.Internal
	public static void register() {
		DynamicRegistries.registerSynced(ModRegistryKeys.STYLING, EnchantmentStyling.CODEC);
	}

	@ApiStatus.Internal
	public static final class Manager {
		/**
		 * Because {@link net.minecraft.enchantment.Enchantment Enchantment} does not have access to an instance of {@link DynamicRegistryManager}
		 * and a {@code registryManager} is required to iterate through the {@link ModRegistryKeys#STYLING} registry to apply text
		 * styling for enchantments on both the client and server, we set this variable to be an empty optional and then fill it
		 * with a {@code registryManager} from the constructor of {@link net.minecraft.world.World World}.
		 *
		 * @see dev.shadowhunter22.enchantmenttextstyling.mixin.world.MixinWorld#getAndSetRegistryManager(MutableWorldProperties, RegistryKey, DynamicRegistryManager, RegistryEntry, Supplier, boolean, boolean, long, int, CallbackInfo) MixinWorld.getAndSetRegistryManager()
		 */
		public static Optional<DynamicRegistryManager> registryManager = Optional.empty();
	}
}
