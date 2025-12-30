package com.linmjie.corebound.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.Set;
import java.util.stream.Collectors;

public class CoreboundUtils {
    public static int randomInt(int i){
        return Math.toIntExact((long) Math.ceil(Math.random() * i));
    }

    public static int randomInt(int min, int max){
        return randomInt(max-min) + min;
    }

    public static String replaceNamespace(String original, String namespace) {
        return namespace + ":" + original.split(":")[1];
    }

    public static Set<String> collectItems(TagKey<Item> tag){
        return BuiltInRegistries.ITEM.getOrCreateTag(tag).stream()
                .map(Holder::getRegisteredName)
                .collect(Collectors.toSet());
    }

    public static Set<String> collectItems(TagKey<Item> tag, String namespace){
        return BuiltInRegistries.ITEM.getOrCreateTag(tag).stream()
                .map(Holder::getRegisteredName)
                .map(itemString -> replaceNamespace(itemString, namespace))
                .collect(Collectors.toSet());
    }

    public static boolean recipeIsTwoByTwo(CraftingRecipe craftingRecipe){
        return craftingRecipe.getIngredients().size() <= 5; //placeholder, doesn't work all the time (slabs)
    }
}