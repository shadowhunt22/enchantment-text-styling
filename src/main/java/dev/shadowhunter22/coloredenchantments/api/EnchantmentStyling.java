//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.coloredenchantments.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class EnchantmentStyling {
    RegistryKey<Enchantment> enchantment;

    private final Identifier enchantmentId;
    private int color;
    private final Optional<EnchantmentStylingCondition> enchantmentStylingCondition;

    private EnchantmentStyling(Identifier enchantmentId, int color, Optional<EnchantmentStylingCondition> enchantmentStylingCondition) {
        this.enchantmentId = enchantmentId;
        this.color = color;
        this.enchantmentStylingCondition = enchantmentStylingCondition;
    }

    public EnchantmentStyling(RegistryKey<Enchantment> enchantment) {
        this.enchantment = enchantment;
        this.enchantmentId = enchantment.getValue();
        this.color = Formatting.GRAY.getColorValue(); // default
        this.enchantmentStylingCondition = Optional.of(new EnchantmentStylingCondition());
    }

    /**
     * The color of the enchantment text.
     * @param value an integer from either calling {@link Formatting#getColorValue()} or supplying an RGB value converted to an integer.
     * Use a tool like <a href="http://www.shodor.org/~efarrow/trunk/html/rgbint.html">shodor.org</a> to convert an RGB to an integer.
     */
    public void color(int value) {
        this.color = value;
    }

    /**
     * This value represents when the custom enchantment styling will be applied.  For example, if you supply a {@code 2} and the
     * level of the enchantment is {@code 2}, then the {@link EnchantmentStyling#color(int)} will be applied.
     */
    public void value(int value) {
        this.enchantmentStylingCondition.ifPresent(condition -> condition.value = Optional.of(value));
    }

    /**
     * This value represents when the custom enchantment styling will be applied.  For example, if you supply a {@code 1} and the
     * level of the enchantment is {@code >=1}, then the {@link EnchantmentStyling#color(int)} will be applied.
     * <br> <br>
     * If, for example, a value of {@code 2} is supplied to {@link EnchantmentStyling#max(int)} as well, then it will
     * evaluate as {@code >=1 && <=2}
     */
    public void min(int value) {
        this.enchantmentStylingCondition.ifPresent(condition -> condition.min = Optional.of(value));
    }

    /**
     * This value represents when the custom enchantment styling will be applied.  For example, if you supply a {@code 3} and the
     * level of the enchantment is {@code <=3}, then the {@link EnchantmentStyling#color(int)} will be applied.
     * <br> <br>
     * If, for example, a value of {@code 1} is supplied to {@link EnchantmentStyling#min(int)} as well, then it will
     * evaluate as {@code >=1 && <=3}.
     */
    public void max(int value) {
        this.enchantmentStylingCondition.ifPresent(condition -> condition.max = Optional.of(value));
    }

    public static Codec<EnchantmentStyling> getCodec() {
        return RecordCodecBuilder.create(builder -> builder.group(
            Identifier.CODEC.fieldOf("enchantment").forGetter(EnchantmentStyling::getEnchantmentId),
            Codec.INT.fieldOf("color").forGetter(EnchantmentStyling::getColor),
            EnchantmentStylingCondition.CODEC.optionalFieldOf("conditions").forGetter(EnchantmentStyling::getEnchantmentStylingCondition)
        ).apply(builder, EnchantmentStyling::new));
    }

    public Identifier getEnchantmentId() {
        return this.enchantmentId;
    }

    public int getColor() {
        return this.color;
    }

    public Optional<EnchantmentStylingCondition> getEnchantmentStylingCondition() {
        return this.enchantmentStylingCondition;
    }

    public static class EnchantmentStylingCondition {
        Optional<Integer> value = Optional.empty();
        Optional<Integer> min = Optional.empty();
        Optional<Integer> max = Optional.empty();

        private EnchantmentStylingCondition(Optional<Integer> value, Optional<Integer> min, Optional<Integer> max) {
        }

        public EnchantmentStylingCondition() {
        }

        public static final Codec<EnchantmentStylingCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.optionalFieldOf("value").forGetter(EnchantmentStylingCondition::getValue),
                Codec.INT.optionalFieldOf("min").forGetter(EnchantmentStylingCondition::getMin),
                Codec.INT.optionalFieldOf("max").forGetter(EnchantmentStylingCondition::getMax)
        ).apply(instance, EnchantmentStylingCondition::new));

        public Optional<Integer> getValue() {
            return this.value;
        }

        public Optional<Integer> getMin() {
            return this.min;
        }

        public Optional<Integer> getMax() {
            return this.max;
        }
    }
}
