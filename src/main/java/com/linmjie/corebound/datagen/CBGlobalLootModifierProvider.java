package com.linmjie.corebound.datagen;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.item.CBItems;
import com.linmjie.corebound.loot.AddItemModifier;
import com.linmjie.corebound.loot.LootItemBlockTagPropertyCondition;
import com.linmjie.corebound.loot.RemoveItemTagModifier;
import com.linmjie.corebound.tags.CBTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class CBGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public CBGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Corebound.MODID);
    }

    @Override
    protected void start() {
        this.add("leaves_to_twigs",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagPropertyCondition.matchesBlockTag(BlockTags.LEAVES).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(CBItems.WOODEN_SHEARS)).build(),
                        LootItemRandomChanceCondition.randomChance(1f).build() },
                        CBItems.TWIG.get()));
        this.add("rocks_from_cobblestone",
                new AddItemModifier(new LootItemCondition[]{
                        LootItemBlockTagPropertyCondition.matchesBlockTag(CBTags.Blocks.DROPS_ROCKS).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.WOODEN_PICKAXE)).build(),
                        LootItemRandomChanceCondition.randomChance(1f).build() },
                        CBItems.ROCK.get(), 2, 5));
        this.add("remove_cobblestone_like_drops_from_cobblestone",
                new RemoveItemTagModifier(new LootItemCondition[]{
                        LootItemBlockTagPropertyCondition.matchesBlockTag(CBTags.Blocks.DROPS_ROCKS).build(),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.WOODEN_PICKAXE)).build(),
                        LootItemRandomChanceCondition.randomChance(1f).build() },
                        CBTags.Items.ROCKS_REPLACE));
    }
}
