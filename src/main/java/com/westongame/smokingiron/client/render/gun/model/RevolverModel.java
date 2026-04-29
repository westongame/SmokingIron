package com.westongame.smokingiron.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.westongame.smokingiron.client.GunModel;
import com.westongame.smokingiron.client.SpecialModels;
import com.westongame.smokingiron.client.render.gun.IOverrideModel;
import com.westongame.smokingiron.client.util.RenderUtil;
import com.westongame.smokingiron.common.Gun;
import com.westongame.smokingiron.item.GunItem;
import com.westongame.smokingiron.item.attachment.IAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 * Modified by zaeonNineZero for Nine Zero's Gun Expansion
 * Ported to SmokingIron NeoForge 1.21.1 (fallback animation only — no CGM Expanded).
 */
public class RevolverModel implements IOverrideModel
{
    @Override
    public void render(float partialTicks, ItemDisplayContext display, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        BakedModel bakedModel = SpecialModels.REVOLVER_BASE.getModel();
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, GunModel.wrap(bakedModel));

        ItemStack attachmentStack = Gun.getAttachment(IAttachment.Type.SCOPE, stack);
        if(!attachmentStack.isEmpty())
        {
            RenderUtil.renderModel(SpecialModels.REVOLVER_RAIL.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        }

        boolean isPlayer = entity != null && entity.equals(Minecraft.getInstance().player);
        boolean isFirstPerson = (display == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || display == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
        boolean correctContext = (isFirstPerson || display == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || display == ItemDisplayContext.THIRD_PERSON_LEFT_HAND);

        Vec3 cylinderRotations = Vec3.ZERO;
        Vec3 cylinderRotOffset = new Vec3(0, -4.43, 0);

        // Cylinder rotates by 60° per shot (cooldown-driven fallback animation).
        if(isPlayer && correctContext && stack.getItem() instanceof GunItem gunItem)
        {
            Gun gun = gunItem.getModifiedGun(stack);
            float cooldownDivider = Math.max((float) gun.getGeneral().getRate() / 5F, 1F);
            float cooldownOffset1 = cooldownDivider - 1.0F;
            float intensity = 1.05F;

            ItemCooldowns tracker = Minecraft.getInstance().player.getCooldowns();
            float cooldown = tracker.getCooldownPercent(stack.getItem(), Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
            cooldown *= cooldownDivider;
            float cooldown_a = cooldown - cooldownOffset1;
            float cooldown_b = Math.min(Math.max(cooldown_a * intensity, 0), 1);
            float cooldown_c = cooldown_b * cooldown_b;

            cylinderRotations = cylinderRotations.add(0, 0, cooldown_c * -60);
        }

        // Static swing-out cylinder mount (stays closed without CGM Expanded).
        poseStack.pushPose();
        RenderUtil.renderModel(SpecialModels.REVOLVER_SWING.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        poseStack.popPose();

        // Rotating cylinder.
        poseStack.pushPose();
        if(isPlayer && cylinderRotations != Vec3.ZERO)
        {
            poseStack.translate(0, cylinderRotOffset.y * 0.0625, 0);
            poseStack.mulPose(Axis.ZN.rotationDegrees((float) cylinderRotations.z));
            poseStack.translate(0, -cylinderRotOffset.y * 0.0625, 0);
        }
        RenderUtil.renderModel(SpecialModels.REVOLVER_CYLINDER.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        poseStack.popPose();

        // Symmetrical chamber and bullet components — three pairs at 60° intervals.
        for(int i = 0; i < 3; i++)
        {
            float chamberRot = (float) cylinderRotations.z + (60 * i);

            // Chamber.
            poseStack.pushPose();
            if(isPlayer)
            {
                poseStack.translate(0, cylinderRotOffset.y * 0.0625, 0);
                poseStack.mulPose(Axis.ZN.rotationDegrees(chamberRot));
                poseStack.translate(0, -cylinderRotOffset.y * 0.0625, 0);
            }
            RenderUtil.renderModel(SpecialModels.REVOLVER_CHAMBERS.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
            poseStack.popPose();

            // Bullet.
            poseStack.pushPose();
            if(isPlayer)
            {
                poseStack.translate(0, cylinderRotOffset.y * 0.0625, 0);
                poseStack.mulPose(Axis.ZN.rotationDegrees(chamberRot));
                poseStack.translate(0, -cylinderRotOffset.y * 0.0625, 0);
            }
            RenderUtil.renderModel(SpecialModels.REVOLVER_BULLETS.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
            poseStack.popPose();
        }
    }
}
