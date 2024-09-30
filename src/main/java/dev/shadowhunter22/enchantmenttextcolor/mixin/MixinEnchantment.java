package dev.shadowhunter22.enchantmenttextcolor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.shadowhunter22.enchantmenttextcolor.api.EnchantmentStyling;
import dev.shadowhunter22.enchantmenttextcolor.api.data.EnchantmentTextDataLoader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Debug(export = true)
@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Inject(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Texts;setStyleIfAbsent(Lnet/minecraft/text/MutableText;Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private static void addEnchantmentColor(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir, @Local MutableText mutableText) {
        EnchantmentTextDataLoader.getEntries().forEach((id, stylings) -> stylings.forEach(styling -> {
            RegistryKey<Enchantment> enchantmentKey = RegistryKey.of(RegistryKeys.ENCHANTMENT, id);

            if (enchantment.matchesId(enchantmentKey.getValue())) {
                setStyle(mutableText, level, styling);
            }
        }));
    }

    // this mod is an example of why I love coding.  this method is an example of why I hate coding :)
    @Unique private static void setStyle(MutableText mutableText, int level, EnchantmentStyling styling) {
        EnchantmentStyling.EnchantmentStylingCondition condition = styling.condition();
        Style style = Style.EMPTY.withColor(styling.color());

        if (!styling.condition().isEmpty()) {
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
    }
}
