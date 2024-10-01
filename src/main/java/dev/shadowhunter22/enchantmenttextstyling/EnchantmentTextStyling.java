//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextstyling;

import dev.shadowhunter22.enchantmenttextstyling.api.data.EnchantmentTextStylingDataLoader;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentTextStyling implements ModInitializer {
	public static final String MOD_ID = "enchantment-text-styling";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EnchantmentTextStylingDataLoader.listener();

		LOGGER.info("Successfully loaded {} mod!", MOD_ID);
	}
}