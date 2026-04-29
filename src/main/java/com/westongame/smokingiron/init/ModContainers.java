package com.westongame.smokingiron.init;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.blockentity.WorkbenchBlockEntity;
import com.westongame.smokingiron.common.container.AttachmentContainer;
import com.westongame.smokingiron.common.container.WorkbenchContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Author: MrCrayfish
 */
public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, Reference.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<WorkbenchContainer>> WORKBENCH = register("workbench", (IContainerFactory<WorkbenchContainer>) (windowId, playerInventory, data) -> {
        WorkbenchBlockEntity workstation = (WorkbenchBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new WorkbenchContainer(windowId, playerInventory, workstation);
    });

    public static final DeferredHolder<MenuType<?>, MenuType<AttachmentContainer>> ATTACHMENTS = register("attachments", AttachmentContainer::new);

    @SuppressWarnings("unchecked")
    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory)
    {
        return (DeferredHolder<MenuType<?>, MenuType<T>>) (DeferredHolder<?, ?>) REGISTER.register(id, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }
}
