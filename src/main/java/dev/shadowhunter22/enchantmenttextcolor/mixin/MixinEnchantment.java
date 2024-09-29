package dev.shadowhunter22.enchantmenttextcolor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Shadow public abstract boolean equals(Object par1);

    @Inject(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Texts;setStyleIfAbsent(Lnet/minecraft/text/MutableText;Lnet/minecraft/text/Style;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private static void addEnchantmentColor(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir, @Local MutableText mutableText) {
        EnchantmentTextDataLoader.getEntries().forEach(((styling, condition) -> {
            RegistryKey<Enchantment> enchantment1 = RegistryKey.of(RegistryKeys.ENCHANTMENT, styling.getEnchantmentId());

            if (enchantment.matchesId(enchantment1.getValue())) {
                Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(styling.getColor()));
            }
        }));
    }
}
