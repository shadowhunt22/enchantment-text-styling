package dev.shadowhunter22.enchantmenttextcolor.test;//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

import dev.shadowhunter22.enchantmenttextcolor.api.datagen.EnchantmentTextColorProvider;
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
		this.addEntry(Enchantments.PROTECTION)
				.color(Formatting.RED.getColorValue())
				.specificCondition(1)
				.add();

		this.addEntry(Enchantments.PROTECTION)
				.color(Formatting.GOLD.getColorValue())
				.specificCondition(2)
				.add();

		this.addEntry(Enchantments.PROTECTION)
				.color(Formatting.YELLOW.getColorValue())
				.specificCondition(3)
				.add();

		this.addEntry(Enchantments.PROTECTION)
				.color(Formatting.GREEN.getColorValue())
				.specificCondition(4)
				.add();

		this.addEntry(Enchantments.BLAST_PROTECTION)
				.color(Formatting.GREEN.getColorValue())
				.min(2)
				.max(3)
				.add();

		this.addEntry(Enchantments.PROJECTILE_PROTECTION)
				.color(Formatting.RED.getColorValue())
				.min(1)
				.max(2)
				.add();

		this.addEntry(Enchantments.FIRE_PROTECTION)
				.color(Formatting.GREEN.getColorValue())
				.min(3)
				.max(4)
				.add();

		// testing to see if the color will still be red without adding .color() to the method chain
		this.addEntry(Enchantments.BINDING_CURSE)
				.add();

		// testing to see if the color will still be gray without adding .color() to the method chain
		this.addEntry(Enchantments.FIRE_ASPECT)
				.add();
	}
}