//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.coloredenchantments.test;

import dev.shadowhunter22.coloredenchantments.api.datagen.v1.EnchantmentTextProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Formatting;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTextData extends EnchantmentTextProvider {
	protected EnchantmentTextData(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, registriesFuture);
	}

	@Override
	public void generate() {
		this.addEntry(Enchantments.PROTECTION)
				.color(Formatting.AQUA.getColorValue())
				.build();
	}
}