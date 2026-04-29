package com.westongame.smokingiron.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.westongame.smokingiron.client.GunModel;
import com.westongame.smokingiron.client.SpecialModels;
import com.westongame.smokingiron.client.handler.ReloadHandler;
import com.westongame.smokingiron.client.render.gun.IOverrideModel;
import com.westongame.smokingiron.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 * Modified by zaeonNineZero for Nine Zero's Gun Expansion
 * Ported to SmokingIron NeoForge 1.21.1 (fallback animation only — no CGM Expanded).
 */
public class DoubleBarreledShotgunModel implements IOverrideModel
{
    @Override
    public void render(float partialTicks, ItemDisplayContext display, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        BakedModel bakedModel = SpecialModels.DOUBLE_BARRELED_SHOTGUN_BASE.getModel();
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, GunModel.wrap(bakedModel));

        boolean isPlayer = entity != null && entity.equals(Minecraft.getInstance().player);
        boolean isFirstPerson = (display == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || display == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
        boolean correctContext = (isFirstPerson || display == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || display == ItemDisplayContext.THIRD_PERSON_LEFT_HAND);

        Vec3 breakRotations = Vec3.ZERO;
        Vec3 breakRotOffset = new Vec3(0, -5.0, 5.0);

        if(isPlayer && correctContext)
        {
            // Break-action opens during reload.
            breakRotations = new Vec3(35 * ReloadHandler.get().getReloadProgress(partialTicks), 0, 0);
        }

        poseStack.pushPose();
        if(isPlayer && isFirstPerson && breakRotations != Vec3.ZERO)
        {
            poseStack.translate(-breakRotOffset.x * 0.0625, breakRotOffset.y * 0.0625, breakRotOffset.z * 0.0625);
            poseStack.mulPose(Axis.XP.rotationDegrees((float) breakRotations.x));
            poseStack.translate(breakRotOffset.x * 0.0625, -breakRotOffset.y * 0.0625, -breakRotOffset.z * 0.0625);
        }
        RenderUtil.renderModel(SpecialModels.DOUBLE_BARRELED_SHOTGUN_BREAK.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        poseStack.popPose();
    }
}
