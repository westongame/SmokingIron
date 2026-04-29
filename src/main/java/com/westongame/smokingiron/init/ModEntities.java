package com.westongame.smokingiron.init;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.entity.GrenadeEntity;
import com.westongame.smokingiron.entity.ProjectileEntity;
import com.westongame.smokingiron.entity.ThrowableGrenadeEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;

/**
 * Author: MrCrayfish
 */
public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(Registries.ENTITY_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ProjectileEntity>> PROJECTILE = registerProjectile("projectile", ProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<GrenadeEntity>> GRENADE = registerBasic("grenade", GrenadeEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableGrenadeEntity>> THROWABLE_GRENADE = registerBasic("throwable_grenade", ThrowableGrenadeEntity::new);

    @SuppressWarnings("unchecked")
    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerBasic(String id, BiFunction<EntityType<T>, Level, T> function)
    {
        return (DeferredHolder<EntityType<?>, EntityType<T>>) (DeferredHolder<?, ?>) REGISTER.register(id, () -> EntityType.Builder.of(function::apply, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .setTrackingRange(100)
                .setUpdateInterval(1)
                .noSummon()
                .fireImmune()
                .setShouldReceiveVelocityUpdates(true).build(id));
    }

    /**
     * Entity registration that prevents the entity from being sent and tracked by clients. Projectiles
     * are rendered separately from Minecraft's entity rendering system and their logic is handled
     * exclusively by the server, why send them to the client. Projectiles also have very short time
     * in the world and are spawned many times a tick. There is no reason to send unnecessary packets
     * when it can be avoided to drastically improve the performance of the game.
     *
     * @param id       the id of the projectile
     * @param function the factory to spawn the projectile for the server
     * @param <T>      an entity that is a projectile entity
     * @return A registry object containing the new entity type
     */
    @SuppressWarnings("unchecked")
    private static <T extends ProjectileEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerProjectile(String id, BiFunction<EntityType<T>, Level, T> function)
    {
        return (DeferredHolder<EntityType<?>, EntityType<T>>) (DeferredHolder<?, ?>) REGISTER.register(id, () -> EntityType.Builder.of(function::apply, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(0)
                .noSummon()
                .fireImmune()
                .build(id));
    }
}
