package dev.shadowhunter22.enchantmenttextcolor.test;//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

import dev.shadowhunter22.enchantmenttextcolor.api.datagen.v1.EnchantmentTextColorProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Formatting;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTextColorData extends EnchantmentTextColorProvider {
	protected EnchantmentTextColorData(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, registriesFuture);
	}

	@Override
	public void generate(RegistryWrapper.WrapperLookup lookup) {
		// testing to see if the color will be red when adding .color() to the method chain
		this.addEntry(Enchantments.AQUA_AFFINITY)
				.color(Formatting.GREEN.getColorValue())
				.add();

		// testing to see if the color will still be red without adding .color() to the method chain
		this.addEntry(Enchantments.BINDING_CURSE)
				.add();

		// testing to see if the color will still be gray without adding .color() to the method chain
		this.addEntry(Enchantments.FIRE_ASPECT)
				.add();
	}
}