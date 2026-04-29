package com.westongame.smokingiron.client;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.client.render.entity.GrenadeRenderer;
import com.westongame.smokingiron.client.render.entity.ProjectileRenderer;
import com.westongame.smokingiron.client.render.entity.ThrowableGrenadeRenderer;
import com.westongame.smokingiron.init.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class GunEntityRenderers
{
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(ModEntities.PROJECTILE.get(), ProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWABLE_GRENADE.get(), ThrowableGrenadeRenderer::new);
    }
}
