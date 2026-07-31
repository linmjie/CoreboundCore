package com.linmjie.corebound.worldgen;

import com.linmjie.corebound.Corebound;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class CBPlacedFeatures {
    public static final ResourceKey<PlacedFeature> TIN_ORE_PLACED_KEY = registerKey("tin_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Hasn't been playtested yet
        register(context, TIN_ORE_PLACED_KEY, configuredFeatures.getOrThrow(CBConfiguredFeatures.OVERWORLD_TIN_ORE_KEY),
                CBOrePlacement.commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(-10), VerticalAnchor.absolute(20))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Corebound.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
