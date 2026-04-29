package com.westongame.smokingiron.item;

import com.westongame.smokingiron.item.attachment.IUnderBarrel;
import com.westongame.smokingiron.item.attachment.impl.UnderBarrel;
import net.minecraft.world.item.ItemStack;

/**
 * A basic under barrel attachment item implementation with color support
 *
 * Author: MrCrayfish
 */
public class UnderBarrelItem extends AttachmentItem implements IUnderBarrel, IColored
{
    private final UnderBarrel underBarrel;
    private final boolean colored;

    public UnderBarrelItem(UnderBarrel underBarrel, Properties properties)
    {
        super(properties);
        this.underBarrel = underBarrel;
        this.colored = true;
    }

    public UnderBarrelItem(UnderBarrel underBarrel, Properties properties, boolean colored)
    {
        super(properties);
        this.underBarrel = underBarrel;
        this.colored = colored;
    }

    @Override
    public UnderBarrel getProperties()
    {
        return this.underBarrel;
    }

    @Override
    public boolean canColor(ItemStack stack)
    {
        return this.colored;
    }

}
