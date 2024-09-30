//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextcolor.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

public record EnchantmentStyling(Identifier id, int color, EnchantmentStylingCondition condition) {
    public static final Codec<EnchantmentStyling> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Identifier.CODEC.fieldOf("enchantment").forGetter(EnchantmentStyling::id),
            Codec.INT.fieldOf("color").forGetter(EnchantmentStyling::color),
            EnchantmentStylingCondition.CODEC.optionalFieldOf("conditions", EnchantmentStylingCondition.EMPTY).forGetter(EnchantmentStyling::condition)
    ).apply(builder, EnchantmentStyling::new));

    public static Builder builder(RegistryKey<Enchantment> entry) {
        return new Builder(entry);
    }

    public static class Builder {
        private final Identifier id;
        private int color;
        private int value;
        private int min;
        private int max;

        public Builder(RegistryKey<Enchantment> entry) {
            this.id = entry.getValue();

            // the following will only work for vanilla enchantments.  this prevents curse enchantments from having their
            // colors replaced if a developer adds an entry but forgets to method chain .color() when extending EnchantmentTextColorProvider
            Optional<RegistryEntry.Reference<Enchantment>> enchantment = BuiltinRegistries.createWrapperLookup().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOptional(entry);

            if (enchantment.isPresent()) { // will not be present for modded enchantments
                TextColor textColor = enchantment.get().value().description().getStyle().getColor();
                this.color = textColor == null ? Formatting.GRAY.getColorValue() : textColor.getRgb();
            } else {
                this.color = Formatting.GRAY.getColorValue();
            }
        }

        public Builder color(int value) {
            this.color = value;
            return this;
        }

        public void value(int value) {
            this.value = value;
        }

        public void min(int value) {
            this.min = value;
        }

        public void max(int value) {
            this.max = value;
        }

        public EnchantmentStyling build() {
            return new EnchantmentStyling(
                    this.id,
                    this.color,
                    new EnchantmentStylingCondition(
                            this.value == 0 ? Optional.empty() : Optional.of(this.value),
                            this.min == 0 ? Optional.empty() : Optional.of(this.min),
                            this.max == 0 ? Optional.empty() : Optional.of(this.max)
                    )
            );
        }
    }

    public record EnchantmentStylingCondition(Optional<Integer> value, Optional<Integer> min, Optional<Integer> max) {
        public static final Codec<EnchantmentStylingCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.INT.optionalFieldOf("value").forGetter(EnchantmentStylingCondition::value),
                Codec.INT.optionalFieldOf("min").forGetter(EnchantmentStylingCondition::min),
                Codec.INT.optionalFieldOf("max").forGetter(EnchantmentStylingCondition::max)
        ).apply(builder, EnchantmentStylingCondition::new));

        public static final EnchantmentStylingCondition EMPTY = new EnchantmentStylingCondition(Optional.empty(), Optional.empty(), Optional.empty());

        public boolean isEmpty() {
            return this.equals(EnchantmentStyling.EnchantmentStylingCondition.EMPTY);
        }
    }
}