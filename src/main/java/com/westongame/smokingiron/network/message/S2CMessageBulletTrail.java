package com.westongame.smokingiron.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.westongame.smokingiron.client.network.ClientPlayHandler;
import com.westongame.smokingiron.common.Gun;
import com.westongame.smokingiron.entity.ProjectileEntity;
import com.westongame.smokingiron.network.BufferUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class S2CMessageBulletTrail
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageBulletTrail> STREAM_CODEC = StreamCodec.of(
            S2CMessageBulletTrail::encode,
            S2CMessageBulletTrail::decode
    );

    private int[] entityIds;
    private Vec3[] positions;
    private Vec3[] motions;
    private ItemStack item;
    private int trailColor;
    private double trailLengthMultiplier;
    private int life;
    private double gravity;
    private int shooterId;
    private boolean enchanted;
    private ParticleOptions particleData;

    public S2CMessageBulletTrail() {}

    public S2CMessageBulletTrail(ProjectileEntity[] spawnedProjectiles, Gun.Projectile projectileProps, int shooterId, ParticleOptions particleData)
    {
        this.positions = new Vec3[spawnedProjectiles.length];
        this.motions = new Vec3[spawnedProjectiles.length];
        this.entityIds = new int[spawnedProjectiles.length];
        for(int i = 0; i < spawnedProjectiles.length; i++)
        {
            ProjectileEntity projectile = spawnedProjectiles[i];
            this.positions[i] = projectile.position();
            this.motions[i] = projectile.getDeltaMovement();
            this.entityIds[i] = projectile.getId();
        }
        this.item = spawnedProjectiles[0].getItem();
        this.enchanted = spawnedProjectiles[0].getWeapon().isEnchanted();
        this.trailColor = this.enchanted ? 0x9C71FF : projectileProps.getTrailColor();
        this.trailLengthMultiplier = projectileProps.getTrailLengthMultiplier();
        this.life = projectileProps.getLife();
        this.gravity = spawnedProjectiles[0].getModifiedGravity();
        this.shooterId = shooterId;
        this.particleData = particleData;
    }

    public S2CMessageBulletTrail(int[] entityIds, Vec3[] positions, Vec3[] motions, ItemStack item, int trailColor, double trailLengthMultiplier, int life, double gravity, int shooterId, boolean enchanted, ParticleOptions particleData)
    {
        this.entityIds = entityIds;
        this.positions = positions;
        this.motions = motions;
        this.item = item;
        this.trailColor = trailColor;
        this.trailLengthMultiplier = trailLengthMultiplier;
        this.life = life;
        this.gravity = gravity;
        this.shooterId = shooterId;
        this.enchanted = enchanted;
        this.particleData = particleData;
    }

    @SuppressWarnings("unchecked")
    private static void encode(RegistryFriendlyByteBuf buf, S2CMessageBulletTrail msg)
    {
        buf.writeInt(msg.entityIds.length);
        for(int i = 0; i < msg.entityIds.length; i++)
        {
            buf.writeInt(msg.entityIds[i]);
            BufferUtil.writeVec3(buf, msg.positions[i]);
            BufferUtil.writeVec3(buf, msg.motions[i]);
        }
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, msg.item);
        buf.writeVarInt(msg.trailColor);
        buf.writeDouble(msg.trailLengthMultiplier);
        buf.writeInt(msg.life);
        buf.writeDouble(msg.gravity);
        buf.writeInt(msg.shooterId);
        buf.writeBoolean(msg.enchanted);
        buf.writeVarInt(BuiltInRegistries.PARTICLE_TYPE.getId(msg.particleData.getType()));
        ((StreamCodec<RegistryFriendlyByteBuf, ParticleOptions>) ((ParticleType<ParticleOptions>) msg.particleData.getType()).streamCodec()).encode(buf, msg.particleData);
    }

    @SuppressWarnings("unchecked")
    private static S2CMessageBulletTrail decode(RegistryFriendlyByteBuf buf)
    {
        int size = buf.readInt();
        int[] entityIds = new int[size];
        Vec3[] positions = new Vec3[size];
        Vec3[] motions = new Vec3[size];
        for(int i = 0; i < size; i++)
        {
            entityIds[i] = buf.readInt();
            positions[i] = BufferUtil.readVec3(buf);
            motions[i] = BufferUtil.readVec3(buf);
        }
        ItemStack item = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
        int trailColor = buf.readVarInt();
        double trailLengthMultiplier = buf.readDouble();
        int life = buf.readInt();
        double gravity = buf.readDouble();
        int shooterId = buf.readInt();
        boolean enchanted = buf.readBoolean();
        ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.byId(buf.readVarInt());
        if(type == null) type = ParticleTypes.CRIT;
        ParticleOptions particleData = ((StreamCodec<RegistryFriendlyByteBuf, ParticleOptions>) ((ParticleType<ParticleOptions>) type).streamCodec()).decode(buf);
        return new S2CMessageBulletTrail(entityIds, positions, motions, item, trailColor, trailLengthMultiplier, life, gravity, shooterId, enchanted, particleData);
    }

    public static void handle(S2CMessageBulletTrail message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleMessageBulletTrail(message));
        context.setHandled(true);
    }

    public int getCount()
    {
        return this.entityIds.length;
    }

    public int[] getEntityIds()
    {
        return this.entityIds;
    }

    public Vec3[] getPositions()
    {
        return this.positions;
    }

    public Vec3[] getMotions()
    {
        return this.motions;
    }

    public int getTrailColor()
    {
        return this.trailColor;
    }

    public double getTrailLengthMultiplier()
    {
        return this.trailLengthMultiplier;
    }

    public int getLife()
    {
        return this.life;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public double getGravity()
    {
        return this.gravity;
    }

    public int getShooterId()
    {
        return this.shooterId;
    }

    public boolean isEnchanted()
    {
        return this.enchanted;
    }

    public ParticleOptions getParticleData()
    {
        return this.particleData;
    }
}
