//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.coloredenchantments.api.data;

import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.shadowhunter22.coloredenchantments.ColoredEnchantments;
import dev.shadowhunter22.coloredenchantments.api.EnchantmentStyling;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiStatus.Internal
public class EnchantmentTextDataLoader implements SimpleSynchronousResourceReloadListener {
    private static final HashMap<EnchantmentStyling, Optional<EnchantmentStyling.EnchantmentStylingCondition>> entries = new HashMap<>();

    public static void listener() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EnchantmentTextDataLoader());
    }

    public static HashMap<EnchantmentStyling, Optional<EnchantmentStyling.EnchantmentStylingCondition>> getEntries() {
        return entries;
    }

    @Override
    public void reload(ResourceManager manager) {
        entries.clear();

        for (Map.Entry<Identifier, Resource> entry : manager.findResources("styling", path -> path.getPath().endsWith(".json")).entrySet()) {
            if (entry.getKey().getNamespace().equals(ColoredEnchantments.MOD_ID)) {
                try (InputStream stream = entry.getValue().getInputStream()) {
                    JsonObject jsonObject = JsonHelper.deserialize(new InputStreamReader(stream));

                    DataResult<EnchantmentStyling> result = EnchantmentStyling.getCodec().parse(JsonOps.INSTANCE, jsonObject);

                    EnchantmentStyling styling = result.resultOrPartial().orElseThrow();

                    entries.put(styling, styling.getEnchantmentStylingCondition());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(ColoredEnchantments.MOD_ID, "styling");
    }
}
