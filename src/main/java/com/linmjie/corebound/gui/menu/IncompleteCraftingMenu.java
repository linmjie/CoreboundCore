package com.linmjie.corebound.gui.menu;

import com.linmjie.corebound.block.ModBlocks;
import com.linmjie.corebound.block.custom.IncompleteCraftingTableBlock;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public class IncompleteCraftingMenu extends CraftingMenu {
    public final int toolsMask;
    private final ContainerLevelAccess access;

    public IncompleteCraftingMenu(int containerId, Inventory playerInventory) {
        super(containerId, playerInventory);
        this.toolsMask = 0;
        this.access = ContainerLevelAccess.NULL;
    }

    public IncompleteCraftingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, int toolsMask) {
        super(containerId, playerInventory, access);
        this.toolsMask = toolsMask;
        this.access = access;
    }

    protected void slotsChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftSlots,
                                            ResultContainer resultSlots, @Nullable RecipeHolder<CraftingRecipe> recipe)
    {
        if (!level.isClientSide) {
            CraftingInput craftinginput = craftSlots.asCraftInput();
            ServerPlayer serverplayer = (ServerPlayer)player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftinginput, level, recipe);
            if (optional.isPresent()) {
                RecipeHolder<CraftingRecipe> recipeholder = (RecipeHolder)optional.get();
                CraftingRecipe craftingrecipe = (CraftingRecipe)recipeholder.value();
                if (resultSlots.setRecipeUsed(level, serverplayer, recipeholder)) {
                    ItemStack assembledStack = craftingrecipe.assemble(craftinginput, level.registryAccess());
                    if (assembledStack.isItemEnabled(level.enabledFeatures())
                            //level.getServer().getSet(
                    ) {
                        itemstack = assembledStack;
                    }
                }
            }

            resultSlots.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }

    }

    @Override
    public void beginPlacingRecipe() {
        super.beginPlacingRecipe();
    }

    @Override
    public void finishPlacingRecipe(RecipeHolder<CraftingRecipe> recipe) {
        super.finishPlacingRecipe(recipe);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.INCOMPLETE_CRAFTING_TABLE.get());
    }
}
