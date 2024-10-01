//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextcolor.api.datagen.v1;

import dev.shadowhunter22.enchantmenttextcolor.api.EnchantmentStyling;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;

public class EnchantmentTextColorProviderBuilder {
    protected static final ArrayList<EnchantmentStyling> entries = new ArrayList<>();
    private final EnchantmentStyling entry;

    protected EnchantmentTextColorProviderBuilder(RegistryKey<Enchantment> entry) {
        this.entry = new EnchantmentStyling(entry);
    }

    public EnchantmentTextColorProviderBuilder color(int value) {
        this.entry.color(value);
        return this;
    }

    public EnchantmentTextColorProviderBuilder specificCondition(int value) {
        this.entry.value(value);
        return this;
    }

    public EnchantmentTextColorProviderBuilder min(int value) {
        this.entry.min(value);
        return this;
    }

    public EnchantmentTextColorProviderBuilder max(int value) {
        this.entry.max(value);

        return this;
    }

    public void add() {
        entries.add(this.entry);
    }
}
