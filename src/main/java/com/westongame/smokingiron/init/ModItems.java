package com.westongame.smokingiron.init;

import com.westongame.smokingiron.Reference;
import com.westongame.smokingiron.common.Attachments;
import com.westongame.smokingiron.common.GunModifiers;
import com.westongame.smokingiron.item.AmmoItem;
import com.westongame.smokingiron.item.GrenadeItem;
import com.westongame.smokingiron.item.GunItem;
import com.westongame.smokingiron.item.ScopeItem;
import com.westongame.smokingiron.item.StockItem;
import com.westongame.smokingiron.item.UnderBarrelItem;
import com.westongame.smokingiron.item.attachment.impl.Stock;
import com.westongame.smokingiron.item.attachment.impl.UnderBarrel;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, Reference.MOD_ID);

    /* Wild West guns */
    public static final DeferredHolder<Item, Item> REVOLVER = REGISTER.register("revolver", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LEVER_ACTION_RIFLE = REGISTER.register("lever_action_rifle", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> HUNTING_RIFLE = REGISTER.register("hunting_rifle", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> DOUBLE_BARRELED_SHOTGUN = REGISTER.register("double_barreled_shotgun", () -> new GunItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> BASIC_BULLET = REGISTER.register("basic_bullet", () -> new AmmoItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ADVANCED_AMMO = REGISTER.register("advanced_bullet", () -> new AmmoItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SHELL = REGISTER.register("shell", () -> new AmmoItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GRENADE = REGISTER.register("grenade", () -> new GrenadeItem(new Item.Properties(), 20 * 4));

    /* Scope Attachments */
    public static final DeferredHolder<Item, Item> SHORT_SCOPE = REGISTER.register("short_scope", () -> new ScopeItem(Attachments.SHORT_SCOPE, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> MEDIUM_SCOPE = REGISTER.register("medium_scope", () -> new ScopeItem(Attachments.MEDIUM_SCOPE, new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LONG_SCOPE = REGISTER.register("long_scope", () -> new ScopeItem(Attachments.LONG_SCOPE, new Item.Properties().stacksTo(1)));

    /* Stock Attachments */
    public static final DeferredHolder<Item, Item> LIGHT_STOCK = REGISTER.register("light_stock", () -> new StockItem(Stock.create(GunModifiers.BETTER_CONTROL), new Item.Properties().stacksTo(1), false));
    public static final DeferredHolder<Item, Item> TACTICAL_STOCK = REGISTER.register("tactical_stock", () -> new StockItem(Stock.create(GunModifiers.STABILISED), new Item.Properties().stacksTo(1), false));
    public static final DeferredHolder<Item, Item> WEIGHTED_STOCK = REGISTER.register("weighted_stock", () -> new StockItem(Stock.create(GunModifiers.SUPER_STABILISED), new Item.Properties().stacksTo(1)));

    /* Under Barrel Attachments */
    public static final DeferredHolder<Item, Item> LIGHT_GRIP = REGISTER.register("light_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.LIGHT_RECOIL), new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SPECIALISED_GRIP = REGISTER.register("specialised_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.REDUCED_RECOIL), new Item.Properties().stacksTo(1)));
}
