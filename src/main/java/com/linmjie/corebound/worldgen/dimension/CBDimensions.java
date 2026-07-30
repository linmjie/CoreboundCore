package com.linmjie.corebound.worldgen.dimension;

import com.linmjie.corebound.Corebound;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

//Just the keys. The actual dimension + dimension type are plain jsons over in
//src/main/resources/data/corebound/dimension(_type)/ since datapack dimensions load themselves.
public class CBDimensions {
    public static final ResourceKey<Level> DARK_WORLD = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(Corebound.MODID, "dark_world"));

    //Only really here so the json and the code can't drift apart silently
    public static final ResourceKey<DimensionType> DARK_WORLD_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(Corebound.MODID, "dark_world"));
}
