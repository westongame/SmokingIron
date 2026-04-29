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
public class LeverActionRifleModel implements IOverrideModel
{
    @Override
    public void render(float partialTicks, ItemDisplayContext display, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        BakedModel bakedModel = SpecialModels.LEVER_ACTION_RIFLE_BASE.getModel();
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, GunModel.wrap(bakedModel));

        // Iron sights.
        RenderUtil.renderModel(SpecialModels.LEVER_ACTION_RIFLE_SIGHTS.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);

        // Scope rail (only with scope attached).
        ItemStack attachmentStack = Gun.getAttachment(IAttachment.Type.SCOPE, stack);
        if(!attachmentStack.isEmpty())
        {
            RenderUtil.renderModel(SpecialModels.LEVER_ACTION_RIFLE_RAIL.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        }

        boolean isPlayer = entity != null && entity.equals(Minecraft.getInstance().player);
        boolean isFirstPerson = (display == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || display == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
        boolean correctContext = (isFirstPerson || display == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || display == ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
        boolean isDisplayed = (display == ItemDisplayContext.FIXED);

        Vec3 leverRotations = Vec3.ZERO;
        Vec3 leverRotOffset = new Vec3(0, -5.8, 5.03);
        Vec3 hammerRotations = Vec3.ZERO;
        Vec3 hammerRotOffset = new Vec3(0, -5.5, 7.9);
        Vec3 boltTranslations = Vec3.ZERO;

        // Fallback animation: cooldown-driven lever cycle.
        if(isPlayer && correctContext && stack.getItem() instanceof GunItem gunItem)
        {
            Gun gun = gunItem.getModifiedGun(stack);
            float cooldownDivider = 2.0F * Math.max((float) gun.getGeneral().getRate() / 11F, 1F);
            float cooldownOffset1 = cooldownDivider - 1.6F;
            float intensity = 2.25F;

            ItemCooldowns tracker = Minecraft.getInstance().player.getCooldowns();
            float cooldown = tracker.getCooldownPercent(stack.getItem(), Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
            cooldown *= cooldownDivider;
            float cooldown_a = cooldown - cooldownOffset1;

            float cooldown_b = Math.min(Math.max(cooldown_a * intensity, 0), 1);
            float cooldown_c = Math.min(Math.max((-cooldown_a * intensity) + intensity, 0), 1);
            float cooldown_d = Math.min(cooldown_b, cooldown_c);

            leverRotations = new Vec3(cooldown_d * 50, 0, 0);
            hammerRotations = new Vec3(((cooldown_c - 1) * 45), 0, 0);
            boltTranslations = new Vec3(0, 0, cooldown_d * 2.0);
        }

        // Lever rotates along x-axis during fire cycle.
        poseStack.pushPose();
        if(isPlayer && correctContext && leverRotations != Vec3.ZERO)
        {
            poseStack.translate(0, leverRotOffset.y * 0.0625, leverRotOffset.z * 0.0625);
            poseStack.mulPose(Axis.XN.rotationDegrees((float) -leverRotations.x));
            poseStack.translate(0, -leverRotOffset.y * 0.0625, -leverRotOffset.z * 0.0625);
        }
        RenderUtil.renderModel(SpecialModels.LEVER_ACTION_RIFLE_LEVER.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        poseStack.popPose();

        // Hammer cocks back and locks during fire cycle.
        poseStack.pushPose();
        if(isPlayer && !isDisplayed)
        {
            poseStack.translate(0, hammerRotOffset.y * 0.0625, hammerRotOffset.z * 0.0625);
            poseStack.mulPose(Axis.XN.rotationDegrees((float) -hammerRotations.x - 45));
            poseStack.translate(0, -hammerRotOffset.y * 0.0625, -hammerRotOffset.z * 0.0625);
        }
        RenderUtil.renderModel(SpecialModels.LEVER_ACTION_RIFLE_HAMMER.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        poseStack.popPose();

        // Bolt slides back and forth.
        poseStack.pushPose();
        if(isPlayer && boltTranslations != Vec3.ZERO)
        {
            poseStack.translate(0, 0, boltTranslations.z * 0.0625);
        }
        RenderUtil.renderModel(SpecialModels.LEVER_ACTION_RIFLE_BOLT.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        poseStack.popPose();
    }
}
