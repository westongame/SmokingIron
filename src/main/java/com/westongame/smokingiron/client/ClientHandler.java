package com.westongame.smokingiron.client;

import com.westongame.smokingiron.GunMod;
import com.westongame.smokingiron.client.handler.*;
import com.westongame.smokingiron.client.GunItemStackRenderer;
import com.westongame.smokingiron.client.render.gun.ModelOverrides;
import com.westongame.smokingiron.client.render.gun.model.DoubleBarreledShotgunModel;
import com.westongame.smokingiron.client.render.gun.model.HuntingRifleModel;
import com.westongame.smokingiron.client.render.gun.model.LeverActionRifleModel;
import com.westongame.smokingiron.client.render.gun.model.RevolverModel;
import com.westongame.smokingiron.client.render.gun.model.SimpleModel;
import com.westongame.smokingiron.client.screen.AttachmentScreen;
import com.westongame.smokingiron.client.screen.WorkbenchScreen;
import com.westongame.smokingiron.client.util.PropertyHelper;
import com.westongame.smokingiron.debug.IEditorMenu;
import com.westongame.smokingiron.debug.client.screen.EditorScreen;
import com.westongame.smokingiron.init.ModDataComponents;
import com.westongame.smokingiron.init.ModBlocks;
import com.westongame.smokingiron.init.ModContainers;
import com.westongame.smokingiron.init.ModEnchantments;
import com.westongame.smokingiron.init.ModItems;
import com.westongame.smokingiron.item.GunItem;
import com.westongame.smokingiron.item.IColored;
import com.westongame.smokingiron.item.attachment.IAttachment;
import com.westongame.smokingiron.network.PacketHandler;
import com.westongame.smokingiron.network.message.C2SMessageAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import org.lwjgl.glfw.GLFW;

import static com.westongame.smokingiron.Reference.MOD_ID;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ClientHandler {

    public static void setup() {
        NeoForge.EVENT_BUS.register(AimingHandler.get());
        NeoForge.EVENT_BUS.register(BulletTrailRenderingHandler.get());
        NeoForge.EVENT_BUS.register(CrosshairHandler.get());
        NeoForge.EVENT_BUS.register(GunRenderingHandler.get());
        NeoForge.EVENT_BUS.register(RecoilHandler.get());
        NeoForge.EVENT_BUS.register(ReloadHandler.get());
        NeoForge.EVENT_BUS.register(ShootingHandler.get());
        NeoForge.EVENT_BUS.register(new PlayerModelHandler());

        if(ModList.get().isLoaded("firstperson"))
        {
            com.westongame.smokingiron.compat.FirstPersonModelCompat.init();
        }

        setupRenderLayers();
        registerColors();
        registerModelOverrides();
    }

    private static void setupRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WORKBENCH.get(), RenderType.cutout());
    }

    private static void registerColors() {
        ItemColor color = (stack, index) -> {
            if (!IColored.isDyeable(stack)) {
                return -1;
            }
            if (index == 0 && stack.has(ModDataComponents.GUN_COLOR.get())) {
                return 0xFF000000 | stack.getOrDefault(ModDataComponents.GUN_COLOR.get(), -1);
            }
            if (index == 0 && stack.getItem() instanceof IAttachment) {
                ItemStack renderingWeapon = GunRenderingHandler.get().getRenderingWeapon();
                if (renderingWeapon != null) {
                    return Minecraft.getInstance().getItemColors().getColor(renderingWeapon, index);
                }
            }
            if (index == 2) // Reticle colour
            {
                return PropertyHelper.getReticleColor(stack);
            }
            return -1;
        };
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item instanceof IColored) {
                Minecraft.getInstance().getItemColors().register(color, item);
            }
        });
    }

    private static void registerModelOverrides() {
        /* Wild West guns — multi-part animated models */
        ModelOverrides.register(ModItems.REVOLVER.get(), new RevolverModel());
        ModelOverrides.register(ModItems.LEVER_ACTION_RIFLE.get(), new LeverActionRifleModel());
        ModelOverrides.register(ModItems.HUNTING_RIFLE.get(), new HuntingRifleModel());
        ModelOverrides.register(ModItems.DOUBLE_BARRELED_SHOTGUN.get(), new DoubleBarreledShotgunModel());
    }

    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        event.register(ModContainers.WORKBENCH.get(), WorkbenchScreen::new);
        event.register(ModContainers.ATTACHMENTS.get(), AttachmentScreen::new);
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.screen == null && event.getAction() == GLFW.GLFW_PRESS) {
            if (KeyBinds.KEY_ATTACHMENTS.isDown()) {
                PacketHandler.getPlayChannel().sendToServer(new C2SMessageAttachments());
            }
        }
    }

    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) manager -> {
            PropertyHelper.resetCache();
        });
    }

    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(MOD_ID, "special/test")));
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        ModItems.REGISTER.getEntries().forEach(entry -> {
            if (entry.get() instanceof GunItem) {
                event.registerItem(new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return new GunItemStackRenderer();
                    }
                }, entry.get());
            }
        });
    }

    public static void registerCreativeTab(IEventBus bus) {
        DeferredRegister<CreativeModeTab> register = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        builder.title(Component.translatable("itemGroup." + MOD_ID));
        builder.icon(() -> {
            ItemStack stack = new ItemStack(ModItems.REVOLVER.get());
            stack.set(ModDataComponents.IGNORE_AMMO.get(), Unit.INSTANCE);
            return stack;
        });
        builder.displayItems((flags, output) ->
        {
            ModItems.REGISTER.getEntries().forEach(registryObject ->
            {
                if (registryObject.get() instanceof GunItem item) {
                    ItemStack stack = new ItemStack(item);
                    stack.set(ModDataComponents.AMMO_COUNT.get(), item.getGun().getGeneral().getMaxAmmo());
                    output.accept(stack);
                    return;
                }
                output.accept(registryObject.get());
            });
            CustomGunManager.fill(output);

            // Add enchanted books for gun enchantments
            var registry = flags.holders().lookupOrThrow(Registries.ENCHANTMENT);
            ResourceKey<Enchantment>[] keys = new ResourceKey[]{
                ModEnchantments.QUICK_HANDS, ModEnchantments.TRIGGER_FINGER,
                ModEnchantments.LIGHTWEIGHT, ModEnchantments.COLLATERAL,
                ModEnchantments.OVER_CAPACITY, ModEnchantments.RECLAIMED,
                ModEnchantments.ACCELERATOR, ModEnchantments.PUNCTURING,
                ModEnchantments.FIRE_STARTER
            };
            for (ResourceKey<Enchantment> key : keys) {
                registry.get(key).ifPresent(holder -> {
                    output.accept(EnchantedBookItem.createForEnchantment(
                        new EnchantmentInstance(holder, holder.value().getMaxLevel())
                    ), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
                });
            }
        });
        register.register("creative_tab", builder::build);
        register.register(bus);
    }

    public static Screen createEditorScreen(IEditorMenu menu) {
        return new EditorScreen(Minecraft.getInstance().screen, menu);
    }

    /* Uncomment for debugging headshot hit boxes */

    /*@SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onRenderLiving(RenderLivingEvent.Post event)
    {
        LivingEntity entity = event.getEntity();
        IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
        if(headshotBox != null)
        {
            AxisAlignedBB box = headshotBox.getHeadshotBox(entity);
            if(box != null)
            {
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), box, 1.0F, 1.0F, 0.0F, 1.0F);

                AxisAlignedBB boundingBox = entity.getBoundingBox().offset(entity.getPositionVec().inverse());
                boundingBox = boundingBox.grow(Config.COMMON.gameplay.growBoundingBoxAmount.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmount.get());
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), boundingBox, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }*/
}
