//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.coloredenchantments.api.datagen.v1;

import dev.shadowhunter22.coloredenchantments.api.EnchantmentStyling;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;

public class EnchantmentTextProviderBuilder {
    protected static final ArrayList<EnchantmentStyling> entries = new ArrayList<>();
    private final EnchantmentStyling entry;

    protected EnchantmentTextProviderBuilder(RegistryKey<Enchantment> entry) {
        this.entry = new EnchantmentStyling(entry);
    }

    public EnchantmentTextProviderBuilder color(int value) {
        this.entry.color(value);
        return this;
    }

    public EnchantmentTextProviderBuilder specificCondition(int value) {
        this.entry.value(value);
        return this;
    }

    public EnchantmentTextProviderBuilder min(int value) {
        this.entry.min(value);
        return this;
    }

    public EnchantmentTextProviderBuilder max(int value) {
        this.entry.max(value);

        return this;
    }

    public void build() {
        entries.add(this.entry);
    }
}
