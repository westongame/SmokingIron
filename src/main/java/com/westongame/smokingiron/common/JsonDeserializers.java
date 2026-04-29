package com.westongame.smokingiron.common;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.westongame.smokingiron.client.util.Easings;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class JsonDeserializers
{
    public static final JsonDeserializer<ItemStack> ITEM_STACK = (json, typeOfT, context) -> {
        JsonObject obj = json.getAsJsonObject();
        ResourceLocation id = ResourceLocation.parse(obj.get("item").getAsString());
        Item item = BuiltInRegistries.ITEM.get(id);
        int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
        return new ItemStack(item, count);
    };
    public static final JsonDeserializer<ResourceLocation> RESOURCE_LOCATION = (json, typeOfT, context) -> ResourceLocation.parse(json.getAsString());
    public static final JsonDeserializer<GripType> GRIP_TYPE = (json, typeOfT, context) -> GripType.getType(ResourceLocation.tryParse(json.getAsString()));
    public static final JsonDeserializer<Easings> EASING = (json, typeOfT, context) -> Easings.byName(json.getAsString());
}
