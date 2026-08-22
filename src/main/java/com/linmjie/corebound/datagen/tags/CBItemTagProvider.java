package com.linmjie.corebound.datagen.tags;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.CBBlocks;
import com.linmjie.corebound.item.CBItems;
import com.linmjie.corebound.tags.CBTags;
import com.simibubi.create.AllTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CBItemTagProvider extends ItemTagsProvider {
    public CBItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                             CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Corebound.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CBTags.Items.NON_STRIPPED_LOGS_THAT_BURN)
                .add(Items.OAK_LOG)
                .add(Items.ACACIA_LOG)
                .add(Items.BIRCH_LOG)
                .add(Items.CHERRY_LOG)
                .add(Items.JUNGLE_LOG)
                .add(Items.DARK_OAK_LOG)
                .add(Items.MANGROVE_LOG)
                .add(Items.CHERRY_LOG)
                .add(Items.SPRUCE_LOG)
                .add(Items.OAK_WOOD)
                .add(Items.ACACIA_WOOD)
                .add(Items.BIRCH_WOOD)
                .add(Items.CHERRY_WOOD)
                .add(Items.JUNGLE_WOOD)
                .add(Items.DARK_OAK_WOOD)
                .add(Items.MANGROVE_WOOD)
                .add(Items.CHERRY_WOOD)
                .add(Items.SPRUCE_WOOD);
        tag(CBTags.Items.STRIPPED_LOGS_THAT_BURN)
                .add(Items.STRIPPED_OAK_LOG)
                .add(Items.STRIPPED_ACACIA_LOG)
                .add(Items.STRIPPED_BIRCH_LOG)
                .add(Items.STRIPPED_CHERRY_LOG)
                .add(Items.STRIPPED_JUNGLE_LOG)
                .add(Items.STRIPPED_DARK_OAK_LOG)
                .add(Items.STRIPPED_MANGROVE_LOG)
                .add(Items.STRIPPED_CHERRY_LOG)
                .add(Items.STRIPPED_SPRUCE_LOG);
        tag(CBTags.Items.ROCK_ADJACENT)
                .add(Items.FLINT)
                .add(CBItems.ROCK.get());
        tag(CBTags.Items.ROCKS_REPLACE)
                .add(Items.COBBLESTONE)
                .add(Items.COBBLED_DEEPSLATE)
                .add(Items.STONE)
                .add(Items.DEEPSLATE)
                .add(Items.BLACKSTONE);
        tag(CBTags.Items.WOODEN_TOOLS)
                .add(Items.WOODEN_HOE)
                .add(Items.WOODEN_SHOVEL)
                .add(Items.WOODEN_PICKAXE)
                .add(Items.WOODEN_AXE)
                .add(Items.WOODEN_SWORD)
                .add(CBItems.SHARP_STICK.get());
        tag(CBTags.Items.STONE_TOOLS)
                .add(Items.STONE_HOE)
                .add(Items.STONE_SHOVEL)
                .add(Items.STONE_PICKAXE)
                .add(Items.STONE_AXE)
                .add(Items.STONE_SWORD);
        tag(CBTags.Items.UNFIRED_CRAFTING_TOOLS)
                .add(CBItems.UNFIRED_SAW.get())
                .add(CBItems.UNFIRED_PLIERS.get())
                .add(CBItems.UNFIRED_HAMMER.get());
        tag(CBTags.Items.CRAFTING_TOOLS)
                .add(CBItems.SAW.get())
                .add(CBItems.PLIERS.get())
                .add(CBItems.HAMMER.get());

        tag(AllTags.AllItemTags.DEPLOYABLE_DRINK.tag)
                .add(CBItems.CANTEEN.get());
        tag(AllTags.AllItemTags.UPRIGHT_ON_BELT.tag)
                .add(CBItems.CANTEEN.get());

        // COMMON

        tag(CBTags.Items.STORAGE_BLOCKS_STEEL)
                .add(CBItems.DEV_STEEL_CRAFTER.get());
        tag(CBTags.Items.PLATES_STEEL)
                .add(CBItems.DEV_STEEL_CRAFTER.get());
        tag(CBTags.Items.INGOTS_STEEL)
                .add(CBItems.DEV_STEEL_CRAFTER.get());
        tag(CBTags.Items.NUGGETS_STEEL)
                .add(CBItems.DEV_STEEL_CRAFTER.get());

        tag(Tags.Items.RAW_MATERIALS)
                .add(CBItems.RAW_TIN.get());
        tag(CBTags.Items.RAW_MATERIALS_TIN)
                .add(CBItems.RAW_TIN.get());

        tag(Tags.Items.NUGGETS)
                .add(CBItems.DEV_STEEL_CRAFTER.get())
                .add(CBItems.TIN_NUGGET.get())
                .add(CBItems.BRONZE_INGOT.get());
        tag(CBTags.Items.NUGGETS_TIN)
                .add(CBItems.TIN_NUGGET.get());
        tag(CBTags.Items.NUGGETS_BRONZE)
                .add(CBItems.BRONZE_NUGGET.get());

        tag(Tags.Items.INGOTS)
                .add(CBItems.DEV_STEEL_CRAFTER.get())
                .add(CBItems.TIN_INGOT.get())
                .add(CBItems.BRONZE_INGOT.get());
        tag(CBTags.Items.INGOTS_TIN)
                .add(CBItems.TIN_INGOT.get());
        tag(CBTags.Items.INGOTS_BRONZE)
                .add(CBItems.BRONZE_INGOT.get());

        tag(Tags.Items.STORAGE_BLOCKS)
                .add(CBBlocks.RAW_TIN_BLOCK.asItem())
                .add(CBBlocks.TIN_BLOCK.asItem())
                .add(CBBlocks.BRONZE_BLOCK.asItem());
        tag(CBTags.Items.STORAGE_BLOCKS_RAW_TIN)
                .add(CBBlocks.RAW_TIN_BLOCK.asItem());
        tag(CBTags.Items.STORAGE_BLOCKS_TIN)
                .add(CBBlocks.TIN_BLOCK.asItem());
        tag(CBTags.Items.STORAGE_BLOCKS_BRONZE)
                .add(CBBlocks.BRONZE_BLOCK.asItem());
    }
}
