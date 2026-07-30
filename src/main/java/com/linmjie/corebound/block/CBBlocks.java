package com.linmjie.corebound.block;

import com.linmjie.corebound.Corebound;
import com.linmjie.corebound.block.custom.DarkPortalBlock;
import com.linmjie.corebound.block.custom.IncompleteCraftingTableBlock;
import com.linmjie.corebound.item.CBItems;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CBBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Corebound.MODID);

    public static final DeferredBlock<Block>

        TIN_ORE = registerBlock("tin_ore",
            () -> new DropExperienceBlock(ConstantInt.of(0),
                    BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops().strength(3.0F, 3.0F))),
        DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
            () -> new DropExperienceBlock(ConstantInt.of(0),
                    BlockBehaviour.Properties.ofFullCopy(TIN_ORE.get())
                    .mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE))),
        RAW_TIN_BLOCK = registerBlock("raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F)));

    public static final DeferredBlock<IncompleteCraftingTableBlock> INCOMPLETE_CRAFTING_TABLE = registerBlock("incomplete_crafting_table",
            () -> new IncompleteCraftingTableBlock(BlockBehaviour.Properties.of().
                    randomTicks().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()));

    //Obsidian-ish numbers so it isn't trivially griefed, glows a bit so you can find it in the dark
    public static final DeferredBlock<DarkPortalBlock> DARK_PORTAL = registerBlock("dark_portal",
            () -> new DarkPortalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops().strength(5.0F, 1200.0F)
                    .sound(SoundType.STONE).lightLevel(state -> 7)));

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        CBItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register (IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
