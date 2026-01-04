package com.linmjie.corebound.gui.menu;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.util.CoreboundUtils;
import com.linmjie.corebound.util.ModTags;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
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
//Important logic at slotsChangedCraftingGrid method
public class IncompleteCraftingMenu extends RecipeBookMenu<CraftingInput, CraftingRecipe>{
    public static final int RESULT_SLOT = 0;
    private static final int CRAFT_SLOT_START = 1;
    private static final int CRAFT_SLOT_END = 10;
    private static final int INV_SLOT_START = 10;
    private static final int INV_SLOT_END = 37;
    private static final int USE_ROW_SLOT_START = 37;
    private static final int USE_ROW_SLOT_END = 46;
    private final CraftingContainer craftSlots;
    private final ResultContainer resultSlots;
    private final ContainerLevelAccess access;
    private final Player player;
    private boolean placingRecipe;

    private final boolean hasSaw;
    private final boolean hasPliers;
    private final boolean hasHammer;

    private final Set<String> sawRecipes;
    private final Set<String> pliersRecipes;
    private final Set<String> hammerRecipes;
    private final Set<String> alwaysAllow;


    public IncompleteCraftingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, false, false, false);
    }

    public IncompleteCraftingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, boolean hasSaw, boolean hasPliers, boolean hasHammer) {
        super(MenuType.CRAFTING, containerId);
        this.craftSlots = new TransientCraftingContainer(this, 3, 3);
        this.resultSlots = new ResultContainer();
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }

        this.hasSaw = hasSaw;
        this.hasPliers = hasPliers;
        this.hasHammer = hasHammer;

        //Manually adding everything
        sawRecipes = new HashSet<>();
        sawRecipes.addAll(CoreboundUtils.collectItems(ItemTags.PLANKS));
        sawRecipes.addAll(CoreboundUtils.collectItems(ModTags.Items.WOODEN_TOOLS, "corebound"));

        pliersRecipes = new HashSet<>();
        pliersRecipes.addAll(CoreboundUtils.collectItems(ModTags.Items.STONE_TOOLS, "corebound"));

        hammerRecipes = new HashSet<>();
        hammerRecipes.add("corebound:cobblestone");

        alwaysAllow = new HashSet<>();
        hammerRecipes.addAll(CoreboundUtils.collectItems(ModTags.Items.UNFIRED_CRAFTING_TOOLS));
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

    //This method is static in the crafting menu vanilla class, otherwise I would've just overridden it
    //I don't want to figure out MIXIN stuff to redirect the static method call T_T
    protected void slotChangedCraftingGrid(Level level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, @Nullable RecipeHolder<CraftingRecipe> recipe) {
        if (!level.isClientSide) {
            CraftingInput craftinginput = craftSlots.asCraftInput();
            ServerPlayer serverplayer = (ServerPlayer)player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftinginput, level, recipe);
            if (optional.isPresent()) {
                RecipeHolder<CraftingRecipe> recipeholder = (RecipeHolder)optional.get();

                String resource = recipeholder.id().toString();

                CraftingRecipe craftingrecipe = (CraftingRecipe) recipeholder.value();

                if (validateRecipe(resource) ||
                        CoreboundUtils.recipeIsTwoByTwo(craftingrecipe)
                ){
                    if (resultSlots.setRecipeUsed(level, serverplayer, recipeholder)) {
                        ItemStack itemstack1 = craftingrecipe.assemble(craftinginput, level.registryAccess());
                        if (itemstack1.isItemEnabled(level.enabledFeatures())) {
                            itemstack = itemstack1;
                        }
                    }
                }
            }

            resultSlots.setItem(0, itemstack);
            this.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, itemstack));
        }

    }

    //Most of the stuff below is the original crafting menu stuff

    public void slotsChanged(Container inventory) {
        if (!this.placingRecipe) {
            this.access.execute((level, blockPos) -> this.slotChangedCraftingGrid(level, this.player, this.craftSlots, this.resultSlots, (RecipeHolder)null));
        }

    }

    public void beginPlacingRecipe() {
        this.placingRecipe = true;
    }

    public void finishPlacingRecipe(RecipeHolder<CraftingRecipe> recipe) {
        this.placingRecipe = false;
        this.access.execute((level, blockPos) -> this.slotChangedCraftingGrid(level, this.player, this.craftSlots, this.resultSlots, recipe));
    }

    public void fillCraftSlotsStackedContents(StackedContents itemHelper) {
        this.craftSlots.fillStackedContents(itemHelper);
    }

    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) {
        return ((CraftingRecipe)recipe.value()).matches(this.craftSlots.asCraftInput(), this.player.level());
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.craftSlots));
    }

    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.INCOMPLETE_CRAFTING_TABLE.get());
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                this.access.execute((p_39378_, p_39379_) -> itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, player));
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 10 && index < 46) {
                if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    public int getResultSlotIndex() {
        return 0;
    }

    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    public int getSize() {
        return 10;
    }

    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    public boolean shouldMoveToInventory(int slotIndex) {
        return slotIndex != this.getResultSlotIndex();
    }
}