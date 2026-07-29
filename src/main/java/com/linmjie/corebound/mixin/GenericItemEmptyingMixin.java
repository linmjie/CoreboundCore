package com.linmjie.corebound.mixin;

import com.linmjie.corebound.component.CBDataComponentTypes;
import com.linmjie.corebound.fluid.CanteenFluidHandler;
import com.linmjie.corebound.item.CBItems;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GenericItemEmptying.class)
public abstract class GenericItemEmptyingMixin {
    @Inject(method = "canItemBeEmptied", at = @At("HEAD"), cancellable = true)
    private static void addCanteenToCanBeEmptied(Level world, ItemStack stack,
                                                 CallbackInfoReturnable<Boolean> cir)
    {
        if (stack.getItem() == CBItems.CANTEEN.get()) {
            cir.setReturnValue(stack.getOrDefault(CBDataComponentTypes.CANTEEN_POTION_COUNT, 0) > 0);
        }
    }

    @Inject(method = "emptyItem", at = @At("HEAD"), cancellable = true)
    private static void addCanteenItemEmptying(Level level, ItemStack stack, boolean simulate,
                                               CallbackInfoReturnable<Pair<FluidStack, ItemStack>> cir)
    {
        if (stack.getItem() == CBItems.CANTEEN.get()) {
            cir.setReturnValue(CanteenFluidHandler.emptyCanteen(stack, simulate));
        }
    }
}
