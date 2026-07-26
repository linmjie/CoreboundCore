package com.linmjie.corebound.datagen.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.data.recipe.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CBRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();

    public CBRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
    }

    public static void registerAllProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        // Obviously once I add all these recipe gens these will be un-commented
        //GENERATORS.add(new CBCrushingRecipeGen(output, registries));
        //GENERATORS.add(new CBMillingRecipeGen(output, registries));
        //GENERATORS.add(new CBCuttingRecipeGen(output, registries));
        //GENERATORS.add(new CBWashingRecipeGen(output, registries));
        //GENERATORS.add(new CBPolishingRecipeGen(output, registries));
        //GENERATORS.add(new CBDeployingRecipeGen(output, registries));
        //GENERATORS.add(new CBMixingRecipeGen(output, registries));
        //GENERATORS.add(new CBCompactingRecipeGen(output, registries));
        //GENERATORS.add(new CBPressingRecipeGen(output, registries));
        //GENERATORS.add(new CBFillingRecipeGen(output, registries));
        //GENERATORS.add(new CBEmptyingRecipeGen(output, registries));
        //GENERATORS.add(new CBHauntingRecipeGen(output, registries));
        //GENERATORS.add(new CBItemApplicationRecipeGen(output, registries));

        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return "Corebound's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        });
    }
}
