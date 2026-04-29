package com.westongame.smokingiron.client;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.client.particle.BloodParticle;
import com.westongame.smokingiron.client.particle.BulletHoleParticle;
import com.westongame.smokingiron.client.particle.TrailParticle;
import com.westongame.smokingiron.init.ModParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ParticleFactoryRegistry
{
    @SubscribeEvent
    public static void onRegisterParticleFactory(RegisterParticleProvidersEvent event)
    {
        event.registerSpecial(ModParticleTypes.BULLET_HOLE.get(), (typeIn, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> new BulletHoleParticle(worldIn, x, y, z, typeIn.getDirection(), typeIn.getPos()));
        event.registerSpriteSet(ModParticleTypes.BLOOD.get(), BloodParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.TRAIL.get(), TrailParticle.Factory::new);
    }
}
