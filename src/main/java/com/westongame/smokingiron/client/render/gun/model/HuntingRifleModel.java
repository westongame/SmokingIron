package com.westongame.smokingiron.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.westongame.smokingiron.client.GunModel;
import com.westongame.smokingiron.client.SpecialModels;
import com.westongame.smokingiron.client.handler.ReloadHandler;
import com.westongame.smokingiron.client.render.gun.IOverrideModel;
import com.westongame.smokingiron.client.util.RenderUtil;
import com.westongame.smokingiron.common.Gun;
import com.westongame.smokingiron.item.attachment.IAttachment;
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
public class HuntingRifleModel implements IOverrideModel
{
    @Override
    public void render(float partialTicks, ItemDisplayContext display, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        BakedModel bakedModel = SpecialModels.HUNTING_RIFLE_BASE.getModel();
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, GunModel.wrap(bakedModel));

        ItemStack attachmentStack = Gun.getAttachment(IAttachment.Type.SCOPE, stack);
        if(attachmentStack.isEmpty())
        {
            RenderUtil.renderModel(SpecialModels.HUNTING_RIFLE_SIGHTS.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        }

        boolean isPlayer = entity != null && entity.equals(Minecraft.getInstance().player);
        boolean isFirstPerson = (display == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || display == ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
        boolean correctContext = (isFirstPerson || display == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || display == ItemDisplayContext.THIRD_PERSON_LEFT_HAND);

        Vec3 breechRotations = Vec3.ZERO;
        Vec3 breechRotOffset = new Vec3(0, -5.45, 4.3);

        if(isPlayer && correctContext)
        {
            breechRotations = new Vec3(25 * ReloadHandler.get().getReloadProgress(partialTicks), 0, 0);
        }

        // Breech opens during reload.
        poseStack.pushPose();
        if(isPlayer && isFirstPerson && breechRotations != Vec3.ZERO)
        {
            poseStack.translate(-breechRotOffset.x * 0.0625, breechRotOffset.y * 0.0625, breechRotOffset.z * 0.0625);
            poseStack.mulPose(Axis.XP.rotationDegrees((float) breechRotations.x));
            poseStack.translate(breechRotOffset.x * 0.0625, -breechRotOffset.y * 0.0625, -breechRotOffset.z * 0.0625);
        }
        RenderUtil.renderModel(SpecialModels.HUNTING_RIFLE_BREECH.getModel(), display, null, stack, parent, poseStack, buffer, light, overlay);
        poseStack.popPose();
    }
}
