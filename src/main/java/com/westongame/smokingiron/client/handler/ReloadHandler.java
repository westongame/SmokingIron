package com.westongame.smokingiron.client.handler;

import com.westongame.smokingiron.client.KeyBinds;
import com.westongame.smokingiron.common.Gun;
import com.westongame.smokingiron.event.GunReloadEvent;
import com.westongame.smokingiron.init.ModDataComponents;
import com.westongame.smokingiron.init.ModSyncedDataKeys;
import com.westongame.smokingiron.item.GunItem;
import com.westongame.smokingiron.network.PacketHandler;
import com.westongame.smokingiron.network.message.C2SMessageReload;
import com.westongame.smokingiron.network.message.C2SMessageUnload;
import com.westongame.smokingiron.util.GunEnchantmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Author: MrCrayfish
 */
public class ReloadHandler
{
    private static ReloadHandler instance;

    public static ReloadHandler get()
    {
        if(instance == null)
        {
            instance = new ReloadHandler();
        }
        return instance;
    }

    private int startReloadTick;
    private int reloadTimer;
    private int prevReloadTimer;
    private int reloadingSlot;

    private ReloadHandler()
    {
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event)
    {
        this.prevReloadTimer = this.reloadTimer;

        Player player = Minecraft.getInstance().player;
        if(player != null)
        {
            if(ModSyncedDataKeys.RELOADING.getValue(player))
            {
                if(this.reloadingSlot != player.getInventory().selected)
                {
                    this.setReloading(false);
                }
            }

            this.updateReloadTimer(player);
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.Key event)
    {
        Player player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if(KeyBinds.KEY_RELOAD.isDown() && event.getAction() == GLFW.GLFW_PRESS)
        {
            this.setReloading(!ModSyncedDataKeys.RELOADING.getValue(player));
        }
        if(KeyBinds.KEY_UNLOAD.consumeClick() && event.getAction() == GLFW.GLFW_PRESS)
        {
            this.setReloading(false);
            PacketHandler.getPlayChannel().sendToServer(new C2SMessageUnload());
        }
    }

    public void setReloading(boolean reloading)
    {
        Player player = Minecraft.getInstance().player;
        if(player != null)
        {
            if(reloading)
            {
                ItemStack stack = player.getMainHandItem();
                if(stack.getItem() instanceof GunItem)
                {
                    if(!stack.has(ModDataComponents.IGNORE_AMMO.get()))
                    {
                        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                        if(stack.getOrDefault(ModDataComponents.AMMO_COUNT.get(), 0) >= GunEnchantmentHelper.getAmmoCapacity(stack, gun))
                            return;
                        if(NeoForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, stack)).isCanceled())
                            return;
                        ModSyncedDataKeys.RELOADING.setValue(player, true);
                        PacketHandler.getPlayChannel().sendToServer(new C2SMessageReload(true));
                        this.reloadingSlot = player.getInventory().selected;
                        NeoForge.EVENT_BUS.post(new GunReloadEvent.Post(player, stack));
                    }
                }
            }
            else
            {
                ModSyncedDataKeys.RELOADING.setValue(player, false);
                PacketHandler.getPlayChannel().sendToServer(new C2SMessageReload(false));
                this.reloadingSlot = -1;
            }
        }
    }

    private void updateReloadTimer(Player player)
    {
        if(ModSyncedDataKeys.RELOADING.getValue(player))
        {
            if(this.startReloadTick == -1)
            {
                this.startReloadTick = player.tickCount + 5;
            }
            if(this.reloadTimer < 5)
            {
                this.reloadTimer++;
            }
        }
        else
        {
            if(this.startReloadTick != -1)
            {
                this.startReloadTick = -1;
            }
            if(this.reloadTimer > 0)
            {
                this.reloadTimer--;
            }
        }
    }

    public int getStartReloadTick()
    {
        return this.startReloadTick;
    }

    public int getReloadTimer()
    {
        return this.reloadTimer;
    }

    public float getReloadProgress(float partialTicks)
    {
        return (this.prevReloadTimer + (this.reloadTimer - this.prevReloadTimer) * partialTicks) / 5F;
    }
}
