//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.coloredenchantments.api.datagen.v1;

import dev.shadowhunter22.coloredenchantments.ColoredEnchantments;
import dev.shadowhunter22.coloredenchantments.api.EnchantmentStyling;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class EnchantmentTextProvider extends FabricCodecDataProvider<EnchantmentStyling> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ColoredEnchantments.MOD_ID + "/EnchantmentTextProvider");

    protected EnchantmentTextProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, "enchantment/styling", EnchantmentStyling.getCodec());
    }

    @Override
    protected void configure(BiConsumer<Identifier, EnchantmentStyling> provider, RegistryWrapper.WrapperLookup lookup) {
        this.generate();

        TreeMap<Identifier, EnchantmentStyling> linkedHashMap = new TreeMap<>();

        for (EnchantmentStyling enchantmentStyling : EnchantmentTextProviderBuilder.entries) {
            Identifier key = Identifier.of(ColoredEnchantments.MOD_ID, enchantmentStyling.getEnchantmentId().getPath());

            if (!linkedHashMap.containsKey(key)) {
                linkedHashMap.put(
                        Identifier.of(ColoredEnchantments.MOD_ID, enchantmentStyling.getEnchantmentId().getPath()),
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
    public abstract void generate();

    /**
     * Add an entry to {@link EnchantmentTextProvider} to give generate a JSON file.  See {@link EnchantmentTextProvider#generate}
     * for implementation details.
     *
     * @param enchantment the RegistryKey of the enchantment to provide styling for
     * @return {@link EnchantmentTextProviderBuilder} a builder to modify text color and set conditions for the enchantment.
     * See {@link EnchantmentTextProvider#generate} for implementation details.
     */
    public EnchantmentTextProviderBuilder addEntry(RegistryKey<Enchantment> enchantment) {
        return new EnchantmentTextProviderBuilder(enchantment);
    }

    @Override
    public String getName() {
        return "EnchantmentTextProvider";
    }
}
