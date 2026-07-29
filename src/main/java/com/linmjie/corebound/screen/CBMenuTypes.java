package com.linmjie.corebound.screen;

import com.linmjie.corebound.Corebound;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CBMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Corebound.MODID);

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
