package com.westongame.smokingiron.init;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.block.WorkbenchBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class ModBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(Registries.BLOCK, Reference.MOD_ID);

    public static final DeferredHolder<Block, Block> WORKBENCH = register("workbench", () -> new WorkbenchBlock(Block.Properties.of().strength(1.5F).sound(SoundType.METAL).mapColor(MapColor.METAL)));

    private static <T extends Block> DeferredHolder<Block, T> register(String id, Supplier<T> blockSupplier) {
        return register(id, blockSupplier, block1 -> new BlockItem(block1, new Item.Properties()));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Block> DeferredHolder<Block, T> register(String id, Supplier<T> blockSupplier, @Nullable Function<T, BlockItem> supplier) {
        DeferredHolder<Block, T> registryObject = (DeferredHolder<Block, T>) REGISTER.register(id, (Supplier<Block>) blockSupplier);
        if (supplier != null) {
            ModItems.REGISTER.register(id, () -> supplier.apply(registryObject.get()));
        }
        return registryObject;
    }
}
