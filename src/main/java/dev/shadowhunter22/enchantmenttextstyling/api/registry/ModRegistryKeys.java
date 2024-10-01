//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextstyling.api.registry;

import dev.shadowhunter22.enchantmenttextstyling.EnchantmentTextStyling;
import dev.shadowhunter22.enchantmenttextstyling.api.EnchantmentStyling;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<EnchantmentStyling>> STYLING = of("styling");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(Identifier.of(EnchantmentTextStyling.MOD_ID, id));
    }
}
