//
// Copyright (c) 2024 by ShadowHunter22. All rights reserved.
// See LICENSE file in the project root for details.
//

package dev.shadowhunter22.enchantmenttextstyling.api.data;

import com.google.gson.JsonArray;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.shadowhunter22.enchantmenttextstyling.EnchantmentTextStyling;
import dev.shadowhunter22.enchantmenttextstyling.api.EnchantmentStyling;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public class EnchantmentTextStylingDataLoader implements SimpleSynchronousResourceReloadListener {
    private static final HashMap<Identifier, List<EnchantmentStyling>> entries = new HashMap<>();

    public static void listener() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EnchantmentTextStylingDataLoader());
    }

    public static HashMap<Identifier, List<EnchantmentStyling>> getEntries() {
        return entries;
    }

    @Override
    public void reload(ResourceManager manager) {
        entries.clear();

        for (Map.Entry<Identifier, Resource> entry : manager.findResources(this.getFabricId().getPath(), path -> path.getPath().endsWith(".json")).entrySet()) {
            if (entry.getKey().getNamespace().equals(EnchantmentTextStyling.MOD_ID)) {
                try (InputStream stream = entry.getValue().getInputStream()) {
                    JsonArray jsonArray = JsonHelper.deserializeArray(new InputStreamReader(stream));

                    jsonArray.forEach(jsonElement -> {
                        DataResult<EnchantmentStyling> result = EnchantmentStyling.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                        EnchantmentStyling styling = result.resultOrPartial().orElseThrow();

                        entries.computeIfAbsent(styling.id(), k -> new ArrayList<>()).add(styling);
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(EnchantmentTextStyling.MOD_ID, "styling");
    }
}
