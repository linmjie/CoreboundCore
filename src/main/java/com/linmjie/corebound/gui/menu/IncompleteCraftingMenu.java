package com.linmjie.corebound.gui.menu;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.CBBlocks;
import com.linmjie.corebound.util.CoreboundUtils;
import com.linmjie.corebound.util.CBTags;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//Temporary solution??? (hopefully)

public class IncompleteCraftingMenu extends CraftingMenu{
    private final boolean hasSaw;
    private final boolean hasPliers;
    private final boolean hasHammer;

    // I swear these will be "recipe tags" later
    private final Set<String> sawRecipes;
    private final Set<String> pliersRecipes;
    private final Set<String> hammerRecipes;
    private final Set<String> alwaysAllow;


    public IncompleteCraftingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, false, false, false);
    }

    public IncompleteCraftingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, boolean hasSaw, boolean hasPliers, boolean hasHammer) {
        super(containerId, playerInventory, access);

        this.hasSaw = hasSaw;
        this.hasPliers = hasPliers;
        this.hasHammer = hasHammer;

        //Manually adding everything
        sawRecipes = new HashSet<>();
        sawRecipes.addAll(CoreboundUtils.collectItems(ItemTags.PLANKS));
        sawRecipes.addAll(CoreboundUtils.collectItems(CBTags.Items.WOODEN_TOOLS, "corebound"));

        pliersRecipes = new HashSet<>();
        pliersRecipes.addAll(CoreboundUtils.collectItems(CBTags.Items.STONE_TOOLS, "corebound"));

        hammerRecipes = new HashSet<>();
        hammerRecipes.add("corebound:cobblestone");

        alwaysAllow = new HashSet<>();
        alwaysAllow.addAll(CoreboundUtils.collectItems(CBTags.Items.UNFIRED_CRAFTING_TOOLS));
    }

    protected boolean validateRecipe(String resource){
        Corebound.LOGGER.info("Has Saw? {}", hasSaw);
        Corebound.LOGGER.info("Has ? {}", hasPliers);
        Corebound.LOGGER.info("Has Hammer? {}", hasHammer);
        if (hasSaw && sawRecipes.contains(resource))
            return true;
        if (hasPliers && pliersRecipes.contains(resource))
            return true;
        if (hasHammer && hammerRecipes.contains(resource))
            return true;
        return alwaysAllow.contains(resource);
    }

    // This method is static in the crafting menu vanilla class, otherwise I would've just overridden it
    // Everything starting here could be done in like a ten line mixin but this is prob safer
    protected void slotChangedCraftingGrid(Level level, Player player,
                                           CraftingContainer craftSlots,
                                           ResultContainer resultSlots,
                                           @Nullable RecipeHolder<CraftingRecipe> recipe)
    {
        if (!level.isClientSide) {
            CraftingInput craftingInput = craftSlots.asCraftInput();
            ServerPlayer serverPlayer = (ServerPlayer)player;
            ItemStack stack = ItemStack.EMPTY;
            var optional = level.getServer()
                    .getRecipeManager()
                    .getRecipeFor(RecipeType.CRAFTING, craftingInput, level, recipe);
            if (optional.isPresent()) {
                RecipeHolder<CraftingRecipe> recipeHolder = optional.get();
                String resource = recipeHolder.id().toString();
                CraftingRecipe craftingRecipe = recipeHolder.value();
                if (validateRecipe(resource) || craftingRecipe.canCraftInDimensions(2, 2)){
                    if (resultSlots.setRecipeUsed(level, serverPlayer, recipeHolder)) {
                        ItemStack assembled = craftingRecipe.assemble(craftingInput, level.registryAccess());
                        if (assembled.isItemEnabled(level.enabledFeatures())) {
                            stack = assembled;
                        }
                    }
                }
            }
            resultSlots.setItem(0, stack);
            this.setRemoteSlot(0, stack);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, stack));
        }

    }

    // just changing slotChangedCraftingGrid(this, ...) to this.slotsChangedCraftingGrid(...) lol

    public void slotsChanged(Container inventory) {
        if (!this.placingRecipe) {
            this.access.execute(
                (level, blockPos) -> this.slotChangedCraftingGrid(level, this.player, this.craftSlots, this.resultSlots, null));
        }
    }

    public void finishPlacingRecipe(RecipeHolder<CraftingRecipe> recipe) {
        this.placingRecipe = false;
        this.access.execute(
            (level, blockPos) -> this.slotChangedCraftingGrid(level, this.player, this.craftSlots, this.resultSlots, recipe));
    }

    public boolean stillValid(Player player) {
        return stillValid(this.access, player, CBBlocks.INCOMPLETE_CRAFTING_TABLE.get());
    }
}