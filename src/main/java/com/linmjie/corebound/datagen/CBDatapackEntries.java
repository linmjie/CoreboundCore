package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.worldgen.CBBiomeModifiers;
import com.linmjie.corebound.worldgen.CBConfiguredFeatures;
import com.linmjie.corebound.worldgen.CBPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CBDatapackEntries extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, CBConfiguredFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, CBPlacedFeatures::bootstrap)
        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, CBBiomeModifiers::bootstrap);

    public CBDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Corebound.MODID));
    }
}
