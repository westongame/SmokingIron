package com.westongame.smokingiron.network.message;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.framework.api.network.MessageContext;
import com.westongame.smokingiron.client.network.ClientPlayHandler;
import com.westongame.smokingiron.common.CustomGun;
import com.westongame.smokingiron.common.CustomGunLoader;
import com.westongame.smokingiron.common.Gun;
import com.westongame.smokingiron.common.NetworkGunManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;

public class S2CMessageUpdateGuns
{
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMessageUpdateGuns> STREAM_CODEC = StreamCodec.of(
            (buf, msg) -> {
                Validate.notNull(NetworkGunManager.get());
                Validate.notNull(CustomGunLoader.get());
                NetworkGunManager.get().writeRegisteredGuns(buf);
                CustomGunLoader.get().writeCustomGuns(buf);
            },
            buf -> {
                S2CMessageUpdateGuns message = new S2CMessageUpdateGuns();
                message.registeredGuns = NetworkGunManager.readRegisteredGuns(buf);
                message.customGuns = CustomGunLoader.readCustomGuns(buf);
                return message;
            }
    );

    private ImmutableMap<ResourceLocation, Gun> registeredGuns;
    private ImmutableMap<ResourceLocation, CustomGun> customGuns;

    public S2CMessageUpdateGuns() {}

    public static void handle(S2CMessageUpdateGuns message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleUpdateGuns(message));
        context.setHandled(true);
    }

    public ImmutableMap<ResourceLocation, Gun> getRegisteredGuns()
    {
        return this.registeredGuns;
    }

    public ImmutableMap<ResourceLocation, CustomGun> getCustomGuns()
    {
        return this.customGuns;
    }
}
