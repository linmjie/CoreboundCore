package com.linmjie.corebound.tags;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecipeTagManager {
    private static final Map<ResourceLocation, Set<ResourceLocation>> TAGS = new HashMap<>();

    private RecipeTagManager() {
    }

    public static boolean contains(RecipeTag tag, ResourceLocation recipeId) {
        return contains(tag.getId(), recipeId);
    }

    public static boolean contains(ResourceLocation tagId, ResourceLocation recipeId) {
        Set<ResourceLocation> recipes = TAGS.get(tagId);

        return recipes != null && recipes.contains(recipeId);
    }

    public static Set<ResourceLocation> get(RecipeTag tag) {
        return get(tag.getId());
    }

    public static Set<ResourceLocation> get(ResourceLocation tagId) {
        return TAGS.getOrDefault(tagId, Set.of());
    }

    static void replace(Map<ResourceLocation, Set<ResourceLocation>> tags) {
        TAGS.clear();
        TAGS.putAll(tags);
    }

    static void clear() {
        TAGS.clear();
    }
}
