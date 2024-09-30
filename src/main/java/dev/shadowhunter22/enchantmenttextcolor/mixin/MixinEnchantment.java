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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Debug(export = true)
@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Inject(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Texts;setStyleIfAbsent(Lnet/minecraft/text/MutableText;Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private static void addEnchantmentColor(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir, @Local MutableText mutableText) {
        EnchantmentTextDataLoader.getEntries().forEach(((id, stylings) -> stylings.forEach((styling -> {
            RegistryKey<Enchantment> enchantmentKey = RegistryKey.of(RegistryKeys.ENCHANTMENT, id);

            if (enchantment.matchesId(enchantmentKey.getValue())) {
                Optional<EnchantmentStyling.EnchantmentStylingCondition> condition = styling.getEnchantmentStylingCondition();

                if (condition.isPresent()) {
                    Optional<Integer> value = condition.get().getValue();
                    Optional<Integer> min = condition.get().getMin();
                    Optional<Integer> max = condition.get().getMax();

                    if (value.isPresent()) {
                        if (level == value.get()) {
                            Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(styling.getColor()));
                        }
                    }

                    if (max.isPresent() && min.isPresent()) {
                        if (level >= min.get() && level <= max.get()) {
                            Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(styling.getColor()));
                        }
                    } else if (min.isPresent()) {
                        if (level >= min.get()) {
                            Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(styling.getColor()));
                        }
                    } else if (max.isPresent()) {
                        if (level <= max.get()) {
                            Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(styling.getColor()));
                        }
                    }
                } else {
                    Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(styling.getColor()));
                }
            }
        }))));
    }
}
