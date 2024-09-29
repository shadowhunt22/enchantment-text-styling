//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextcolor.api.datagen.v1;

import dev.shadowhunter22.enchantmenttextcolor.EnchantmentTextColor;
import dev.shadowhunter22.enchantmenttextcolor.api.registry.ModRegistryKeys;
import dev.shadowhunter22.enchantmenttextcolor.api.EnchantmentStyling;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class EnchantmentTextColorProvider extends FabricCodecDataProvider<EnchantmentStyling> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnchantmentTextColor.MOD_ID + "/EnchantmentTextColorProvider");

    protected EnchantmentTextColorProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, ModRegistryKeys.STYLING, EnchantmentStyling.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, EnchantmentStyling> provider, RegistryWrapper.WrapperLookup lookup) {
        this.generate(lookup);

        TreeMap<Identifier, EnchantmentStyling> linkedHashMap = new TreeMap<>();

        for (EnchantmentStyling enchantmentStyling : EnchantmentTextColorProviderBuilder.entries) {
            Identifier key = enchantmentStyling.getEnchantmentId();

            if (!linkedHashMap.containsKey(key)) {
                linkedHashMap.put(
                        Identifier.of(EnchantmentTextColor.MOD_ID, key.getNamespace() + "/" + enchantmentStyling.getEnchantmentId().getPath()),
                        enchantmentStyling
                );
            } else {
                LOGGER.warn("Found duplicate entry for {}. Ignoring.", key);
            }
        }

        linkedHashMap.forEach(provider);
    }

    /**
     * Generate json files for enchantments.  Here is an example of how to generate a JSON file for an enchantment:
     *
     * <pre>
     * {@code
     * public class EnchantmentTextData extends EnchantmentTextProvider {
     *     protected EnchantmentTextData(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
     *      super(dataOutput, registriesFuture);
     * }
     *
     *  @Override
     *  public void generate() {
     *      this.addEntry(Enchantments.PROTECTION)
     *              // It is required to call this or a default value will apply.
     * 	        .color(Formatting.AQUA.getColorValue())
     *
     * 	        // These chained methods are optional.  See further documentation below concerning these values
     * 	        .specificCondition(0)
     * 	        .min(0)
     * 	        .max(2)
     *
     * 	        // This is required to be called in order to generate the JSON file
     * 	        .build();
     *  }
     * }
     * }
     * </pre>
     *
     * See also {@link EnchantmentStyling#color(int)} <br>
     * See also {@link EnchantmentStyling#value(int)} <br>
     * See also {@link EnchantmentStyling#min(int)} <br>
     * See also {@link EnchantmentStyling#max(int)}
     */
    public abstract void generate(RegistryWrapper.WrapperLookup lookup);

    /**
     * Add an entry to {@link EnchantmentTextColorProvider} to give generate a JSON file.  See {@link EnchantmentTextColorProvider#generate}
     * for implementation details.
     *
     * @param enchantment the RegistryKey of the enchantment to provide styling for
     * @return {@link EnchantmentTextColorProviderBuilder} a builder to modify text color and set conditions for the enchantment.
     * See {@link EnchantmentTextColorProvider#generate} for implementation details.
     */
    public EnchantmentTextColorProviderBuilder addEntry(RegistryKey<Enchantment> enchantment) {
        return new EnchantmentTextColorProviderBuilder(enchantment);
    }

    @Override
    public String getName() {
        return "EnchantmentTextProvider";
    }
}
