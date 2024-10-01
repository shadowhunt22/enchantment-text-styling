//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextstyling.api.datagen;

import dev.shadowhunter22.enchantmenttextstyling.api.EnchantmentStyling;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Optional;

public class EnchantmentTextStylingProviderBuilder {
    protected static final ArrayList<EnchantmentStyling> entries = new ArrayList<>();
    private final EnchantmentStyling.Builder entry;

    protected EnchantmentTextStylingProviderBuilder(RegistryKey<Enchantment> entry) {
        // the following will only work for vanilla enchantments
        Optional<RegistryEntry.Reference<Enchantment>> enchantment = BuiltinRegistries.createWrapperLookup().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOptional(entry);

        int color;

        if (enchantment.isPresent()) {
            TextColor textColor = enchantment.get().value().description().getStyle().getColor();
            color = textColor == null ? Formatting.GRAY.getColorValue() : textColor.getRgb();
        } else {
            color = Formatting.GRAY.getColorValue();
        }

        this.entry = EnchantmentStyling.builder(entry);
        this.entry.color(color);
    }

    /**
     * The color of the enchantment text.
     * @param value an integer from either calling {@link Formatting#getColorValue()} or supplying an RGB value converted to an integer.
     * Use a tool like <a href="http://www.shodor.org/~efarrow/trunk/html/rgbint.html">shodor.org</a> to convert an RGB to an integer.
     * <br> <br>
     *
     * @see EnchantmentStyling.Builder#color(int)
     */
    public EnchantmentTextStylingProviderBuilder color(int value) {
        this.entry.color(value);
        return this;
    }

    /**
     * Bold text
     *
     * @see EnchantmentStyling.Builder#bold(boolean)
     */
    public EnchantmentTextStylingProviderBuilder bold() {
        this.entry.bold(true);
        return this;
    }

    /**
     * Italic text
     *
     * @see EnchantmentStyling.Builder#italic(boolean)
     */
    public EnchantmentTextStylingProviderBuilder italic() {
        this.entry.italic(true);
        return this;
    }

    /**
     * Underlined text
     *
     * @see EnchantmentStyling.Builder#underlined(boolean)
     */
    public EnchantmentTextStylingProviderBuilder underlined() {
        this.entry.underlined(true);
        return this;
    }

    /**
     * Strikethrough text
     *
     * @see EnchantmentStyling.Builder#strikethrough(boolean)
     */
    public EnchantmentTextStylingProviderBuilder strikethrough() {
        this.entry.strikethrough(true);
        return this;
    }

    /**
     * Obfuscated text
     *
     * @see EnchantmentStyling.Builder#obfuscated(boolean)
     */
    public EnchantmentTextStylingProviderBuilder obfuscated() {
        this.entry.obfuscated(true);
        return this;
    }

    /**
     * This value represents when the custom enchantment styling will be applied.  For example, if you supply a {@code 2} and the
     * level of the enchantment is {@code 2}, then the {@link dev.shadowhunter22.enchantmenttextstyling.api.EnchantmentStyling.EnchantmentTextStyles#color} will be applied to the text of the
     * enchantment.
     *
     * @see EnchantmentStyling.Builder#value(int)
     */
    public EnchantmentTextStylingProviderBuilder specificCondition(int value) {
        this.entry.value(value);
        return this;
    }

    /**
     * This value represents when the custom enchantment styling will be applied.  For example, if you supply a {@code 1},
     * it will evaluate as follows:
     *
     * <pre>{@code if (level >= 1) {
     *      // apply text styling
     * }}</pre>
     *
     * <br>
     *
     * If, for example, a value of {@code 2} is also supplied to {@link EnchantmentStyling.Builder#max(int)}, then it will
     * evaluate as follows:
     *
     * <pre>{@code if (level >= 1 && level <= 2) {
     *     // apply text styling
     * }}</pre>
     *
     * @see EnchantmentStyling.Builder#min(int)
     */
    public EnchantmentTextStylingProviderBuilder min(int value) {
        this.entry.min(value);
        return this;
    }

    /**
     * This value represents when the custom enchantment styling will be applied.  For example, if you supply a {@code 2},
     * it will evaluate as follows:
     *
     * <pre>{@code if (level <= 2) {
     *      // apply text styling
     * }}</pre>
     *
     * <br>
     *
     * If, for example, a value of {@code 1} is also supplied to {@link EnchantmentStyling.Builder#min(int)}, then it will
     * evaluate as follows:
     *
     * <pre>{@code if (level >= 1 && level <= 2) {
     *     // apply text styling
     * }}</pre>
     *
     * @see EnchantmentStyling.Builder#max(int)
     */
    public EnchantmentTextStylingProviderBuilder max(int value) {
        this.entry.max(value);
        return this;
    }

    /**
     * Build and add {@link EnchantmentTextStylingProviderBuilder#entry} to {@link EnchantmentTextStylingProviderBuilder#entries}.
     * Required in order to generate the JSON file.
     */
    public void add() {
        entries.add(this.entry.build());
    }
}
