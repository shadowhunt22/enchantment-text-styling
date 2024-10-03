package dev.shadowhunter22.enchantmenttextstyling.mixin.enchantment;

import com.llamalad7.mixinextras.sugar.Local;
import dev.shadowhunter22.enchantmenttextstyling.api.EnchantmentStyling;
import dev.shadowhunter22.enchantmenttextstyling.registry.ModRegistryKeys;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Inject(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Texts;setStyleIfAbsent(Lnet/minecraft/text/MutableText;Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private static void addEnchantmentColor(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir, @Local MutableText mutableText) {
        ModRegistryKeys.Manager.registryManager.flatMap(k -> k.getOptional(ModRegistryKeys.STYLING)).ifPresent(registry -> registry.forEach(entry -> {
            if (enchantment.matchesId(entry.id())) {
                setStyle(mutableText, level, entry);
            }
        }));
    }

    // this mod is an example of why I love coding.  this method is an example of why I hate coding :)
    @Unique private static void setStyle(MutableText mutableText, int level, EnchantmentStyling styling) {
        List<EnchantmentStyling.EnchantmentTextStyles> styles = styling.styles().stream().toList();

        styles.forEach(textStyles -> {
            Style style = getStyles(textStyles);
            EnchantmentStyling.EnchantmentStylingCondition condition = textStyles.condition();

            if (!condition.isEmpty()) {
                Optional<Integer> value = condition.value();
                Optional<Integer> min = condition.min();
                Optional<Integer> max = condition.max();

                if (value.isPresent()) {
                    if (level == value.get()) {
                        Texts.setStyleIfAbsent(mutableText, style);
                    }
                }

                if (max.isPresent() && min.isPresent()) {
                    if (level >= min.get() && level <= max.get()) {
                        Texts.setStyleIfAbsent(mutableText, style);
                    }
                } else if (min.isPresent()) {
                    if (level >= min.get()) {
                        Texts.setStyleIfAbsent(mutableText, style);
                    }
                } else if (max.isPresent()) {
                    if (level <= max.get()) {
                        Texts.setStyleIfAbsent(mutableText,style);
                    }
                }
            } else {
                Texts.setStyleIfAbsent(mutableText, style);
            }
        });
    }

    @Unique private static Style getStyles(EnchantmentStyling.EnchantmentTextStyles styles) {
        Style style = Style.EMPTY;

        style = style.withColor(styles.color());
        style = style.withBold(styles.unwrapNullableBoolean(styles.bold()));
        style = style.withItalic(styles.unwrapNullableBoolean(styles.italic()));
        style = style.withUnderline(styles.unwrapNullableBoolean(styles.underlined()));
        style = style.withStrikethrough(styles.unwrapNullableBoolean(styles.strikethrough()));
        style = style.withObfuscated(styles.unwrapNullableBoolean(styles.obfuscated()));

        return style;
    }
}