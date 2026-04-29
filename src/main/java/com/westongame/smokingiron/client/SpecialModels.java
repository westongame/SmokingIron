package com.westongame.smokingiron.client;

import com.westongame.smokingiron.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public enum SpecialModels
{
    FLAME("flame"),

    /* Revolver */
    REVOLVER_BASE("gun/revolver/revolver_base"),
    REVOLVER_RAIL("gun/revolver/revolver_rail"),
    REVOLVER_SWING("gun/revolver/revolver_swing_out"),
    REVOLVER_CYLINDER("gun/revolver/revolver_cylinder"),
    REVOLVER_CHAMBERS("gun/revolver/revolver_chambers"),
    REVOLVER_BULLETS("gun/revolver/revolver_bullets"),

    /* NZGE port: Lever-Action Rifle */
    LEVER_ACTION_RIFLE_BASE("gun/lever_action_rifle/lever_action_rifle_base"),
    LEVER_ACTION_RIFLE_BASE_1("gun/lever_action_rifle/lever_action_rifle_base_1"),
    LEVER_ACTION_RIFLE_LEVER("gun/lever_action_rifle/lever_action_rifle_lever"),
    LEVER_ACTION_RIFLE_HAMMER("gun/lever_action_rifle/lever_action_rifle_hammer"),
    LEVER_ACTION_RIFLE_BOLT("gun/lever_action_rifle/lever_action_rifle_bolt"),
    LEVER_ACTION_RIFLE_SIGHTS("gun/lever_action_rifle/lever_action_rifle_rear_sights"),
    LEVER_ACTION_RIFLE_SIGHTS_1("gun/lever_action_rifle/lever_action_rifle_rear_sights_1"),
    LEVER_ACTION_RIFLE_RAIL("gun/lever_action_rifle/lever_action_rifle_rail"),
    LEVER_ACTION_RIFLE_RAIL_1("gun/lever_action_rifle/lever_action_rifle_rail_1"),

    /* NZGE port: Hunting Rifle */
    HUNTING_RIFLE_BASE("gun/hunting_rifle/hunting_rifle_base"),
    HUNTING_RIFLE_BREECH("gun/hunting_rifle/hunting_rifle_breech"),
    HUNTING_RIFLE_SIGHTS("gun/hunting_rifle/hunting_rifle_rear_sight"),

    /* NZGE port: Double Barreled Shotgun */
    DOUBLE_BARRELED_SHOTGUN_BASE("gun/double_barreled_shotgun/double_barreled_shotgun_base"),
    DOUBLE_BARRELED_SHOTGUN_BREAK("gun/double_barreled_shotgun/double_barreled_shotgun_break");

    /**
     * The location of an item model in the [MOD_ID]/models/special/[NAME] folder
     */
    private final ModelResourceLocation modelLocation;

    /**
     * Cached model
     */
    private BakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModels(String modelName)
    {
        this.modelLocation = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "special/" + modelName));
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    public BakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            this.cachedModel = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
        }
        return this.cachedModel;
    }

    /**
     * Registers the special models into the Forge Model Bakery. This is only called once on the
     * load of the game.
     */
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event)
    {
        for(SpecialModels model : values())
        {
            event.register(model.modelLocation);
        }
    }

    /**
     * Clears the cached BakedModel since it's been rebuilt. This is needed since the models may
     * have changed when a resource pack was applied, or if resources are reloaded.
     */
    @SubscribeEvent
    public static void onBake(ModelEvent.BakingCompleted event)
    {
        for(SpecialModels model : values())
        {
            model.cachedModel = null;
        }
    }
}
