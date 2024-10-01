//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.coloredenchantments;

import dev.shadowhunter22.coloredenchantments.api.data.EnchantmentTextDataLoader;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColoredEnchantments implements ModInitializer {
	public static final String MOD_ID = "colored-enchantments";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EnchantmentTextDataLoader.listener();

		LOGGER.info("Successfully loaded {} mod!", MOD_ID);
	}
}