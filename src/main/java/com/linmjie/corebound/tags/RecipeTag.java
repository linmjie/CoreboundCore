package com.linmjie.corebound.tags;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class RecipeTag {
    private final ResourceLocation id;

    private RecipeTag(ResourceLocation resourceLocation) {
        this.id = resourceLocation;
    }

    public static RecipeTag create(String namespace, String path) {
        return new RecipeTag(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean contains(ResourceLocation resourceLocation) {
        return RecipeTagManager.contains(this, resourceLocation);
    }

    public Set<ResourceLocation> values() {
        return RecipeTagManager.get(this);
    }

    @Override
    public String toString() {
        return "RecipeTag[" + this.id + "]";
    }
}
