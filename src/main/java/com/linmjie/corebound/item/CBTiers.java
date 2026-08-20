package com.linmjie.corebound.item;

import com.linmjie.corebound.util.CBTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class CBTiers {
    public static final Tier
        BRONZE = new SimpleTier(
            CBTags.Blocks.INCORRECT_FOR_BRONZE_TOOL,
            250, 6f, 2.5f, 18,
            () -> Ingredient.of(CBTags.Items.INGOTS_BRONZE)
        );
}
