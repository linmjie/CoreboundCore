package com.linmjie.corebound.datagen.recipe;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.item.ModItems;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CBFillingRecipeGen extends FillingRecipeGen {
    public CBFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Corebound.MODID);
    }
}
