package com.westongame.smokingiron.item.attachment;

import com.westongame.smokingiron.item.attachment.impl.Barrel;

/**
 * An interface to turn an any item into a barrel attachment. This is useful if your item extends a
 * custom item class otherwise {@link com.westongame.smokingiron.item.BarrelItem} can be used instead of
 * this interface.
 * <p>
 * Author: Ocelot, MrCrayfish
 */
public interface IBarrel extends IAttachment<Barrel>
{
    /**
     * @return The type of this attachment
     */
    @Override
    default Type getType()
    {
        return Type.BARREL;
    }
}
