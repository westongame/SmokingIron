package com.westongame.smokingiron.init;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.blockentity.WorkbenchBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class ModTileEntities
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WorkbenchBlockEntity>> WORKBENCH = register("workbench", WorkbenchBlockEntity::new, () -> new Block[]{ModBlocks.WORKBENCH.get()});

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, BlockEntityType.BlockEntitySupplier<T> factoryIn, Supplier<Block[]> validBlocksSupplier)
    {
        return (DeferredHolder<BlockEntityType<?>, BlockEntityType<T>>) (DeferredHolder<?, ?>) REGISTER.register(id, () -> BlockEntityType.Builder.of(factoryIn, validBlocksSupplier.get()).build(null));
    }
}
