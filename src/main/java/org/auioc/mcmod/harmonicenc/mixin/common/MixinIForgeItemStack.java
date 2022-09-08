package org.auioc.mcmod.harmonicenc.mixin.common;

import org.auioc.mcmod.harmonicenc.utils.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.extensions.IForgeItemStack;

@Mixin(value = IForgeItemStack.class)
public interface MixinIForgeItemStack {

    // @org.spongepowered.asm.mixin.Debug(export = true, print = true)
    @Overwrite
    default boolean canPerformAction(ToolAction toolAction) {
        if (!EnchantmentHelper.canPerformAction(self(), toolAction)) {
            return false;
        }
        return self().getItem().canPerformAction(self(), toolAction);
    }

    @Shadow
    ItemStack self();

}
