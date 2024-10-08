//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextstyling.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EnchantmentStyling(Identifier id, List<EnchantmentTextStyles> styles) {
	public static final Codec<EnchantmentStyling> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Identifier.CODEC.fieldOf("enchantment").forGetter(EnchantmentStyling::id),
			EnchantmentTextStyles.CODEC.listOf().fieldOf("styles").forGetter(EnchantmentStyling::styles)
	).apply(builder, EnchantmentStyling::new));

	public static Builder builder(RegistryKey<Enchantment> entry) {
		return new Builder(entry);
	}

	public static class Builder {
		private final Identifier id;

		private int color;
		private Boolean bold;
		private Boolean italic;
		private Boolean underlined;
		private Boolean strikethrough;
		private Boolean obfuscated;

		private Integer value;
		private Integer min;
		private Integer max;

		public Builder(RegistryKey<Enchantment> entry) {
			this.id = entry.getValue();

			// the following will only work for vanilla enchantments.  this prevents curse enchantments from having their
			// colors replaced if a developer adds an entry but forgets to method chain .color() when extending EnchantmentTextColorProvider.
			//
			// this also ensures no null values will be passed to EnchantmentStyling.EnchantmentTextStyles
			Optional<RegistryEntry.Reference<Enchantment>> enchantment = BuiltinRegistries.createWrapperLookup().getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(entry);

			if (enchantment.isPresent()) { // will not be present for modded enchantments
				TextColor textColor = enchantment.get().value().description().getStyle().getColor();
				this.color = textColor == null ? Formatting.GRAY.getColorValue() : textColor.getRgb();
			} else {
				this.color = Formatting.GRAY.getColorValue();
			}
		}

		public void color(int value) {
			this.color = value;
		}

		public void bold(boolean value) {
			this.bold = value;
		}

		public void italic(boolean value) {
			this.italic = value;
		}

		public void underlined(boolean value) {
			this.underlined = value;
		}

		public void strikethrough(boolean value) {
			this.strikethrough = value;
		}

		public void obfuscated(boolean value) {
			this.obfuscated = value;
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
			ArrayList<EnchantmentTextStyles> list = new ArrayList<>();
			list.add(
					new EnchantmentTextStyles(
							new EnchantmentStylingCondition(
									Optional.ofNullable(this.value),
									Optional.ofNullable(this.min),
									Optional.ofNullable(this.max)
							),
							this.color,
							Optional.ofNullable(this.bold),
							Optional.ofNullable(this.italic),
							Optional.ofNullable(this.underlined),
							Optional.ofNullable(this.strikethrough),
							Optional.ofNullable(this.obfuscated)
					)
			);

			return new EnchantmentStyling(
					this.id,
					list
			);
		}
	}

	public record EnchantmentTextStyles(EnchantmentStylingCondition condition, int color, Optional<Boolean> bold, Optional<Boolean> italic, Optional<Boolean> underlined, Optional<Boolean> strikethrough, Optional<Boolean> obfuscated) {
		public static final Codec<EnchantmentTextStyles> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				EnchantmentStylingCondition.CODEC.optionalFieldOf("condition", EnchantmentStylingCondition.EMPTY).forGetter(EnchantmentTextStyles::condition),
				Codec.INT.fieldOf("color").forGetter(EnchantmentTextStyles::color),
				Codec.BOOL.optionalFieldOf("bold").forGetter(EnchantmentTextStyles::bold),
				Codec.BOOL.optionalFieldOf("italic").forGetter(EnchantmentTextStyles::italic),
				Codec.BOOL.optionalFieldOf("underlined").forGetter(EnchantmentTextStyles::underlined),
				Codec.BOOL.optionalFieldOf("strikethrough").forGetter(EnchantmentTextStyles::strikethrough),
				Codec.BOOL.optionalFieldOf("obfuscated").forGetter(EnchantmentTextStyles::obfuscated)
		).apply(builder, EnchantmentTextStyles::new));

		/**
		 * Get the styling that is associated with the enchantment {@link EnchantmentStyling#id}.  It is safe to assume
		 * that the following methods will return {@code true} or {@code false}:
		 *
		 * <li> {@link Style#withBold(Boolean)}
		 * <li> {@link Style#withItalic(Boolean)}
		 * <li> {@link Style#withUnderline(Boolean)}
		 * <li> {@link Style#withStrikethrough(Boolean)}
		 * <li> {@link Style#withObfuscated(Boolean)}
		 *
		 * @return {@link Style}
		 */
		public Style getStyling() {
			Style style = Style.EMPTY;

			style = style.withColor(this.color());
			style = style.withBold(this.unwrapNullableBoolean(this.bold()));
			style = style.withItalic(this.unwrapNullableBoolean(this.italic()));
			style = style.withUnderline(this.unwrapNullableBoolean(this.underlined()));
			style = style.withStrikethrough(this.unwrapNullableBoolean(this.strikethrough()));
			style = style.withObfuscated(this.unwrapNullableBoolean(this.obfuscated()));

			return style;
		}

		private boolean unwrapNullableBoolean(Optional<Boolean> value) {
			return value.orElse(false);
		}
	}

	public record EnchantmentStylingCondition(Optional<Integer> value, Optional<Integer> min, Optional<Integer> max) {
		public static final Codec<EnchantmentStylingCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				Codec.INT.optionalFieldOf("value").forGetter(EnchantmentStylingCondition::value),
				Codec.INT.optionalFieldOf("min").forGetter(EnchantmentStylingCondition::min),
				Codec.INT.optionalFieldOf("max").forGetter(EnchantmentStylingCondition::max)
		).apply(builder, EnchantmentStylingCondition::new));

		public static final EnchantmentStylingCondition EMPTY = new EnchantmentStylingCondition(Optional.empty(), Optional.empty(), Optional.empty());

		public boolean isPresent() {
			return !this.equals(EnchantmentStyling.EnchantmentStylingCondition.EMPTY);
		}
	}
}