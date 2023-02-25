package org.auioc.mcmod.harmonicench.mixin.common;

import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.extensions.IForgeItemStack;

@Mixin(value = IForgeItemStack.class, remap = false)
public interface MixinIForgeItemStack {

    /**
     * @author WakelessSloth56
     * @reason {@link org.auioc.mcmod.harmonicench.api.enchantment.IToolActionControllerEnchantment#canPerformAction}
     */
    @Overwrite
    default boolean canPerformAction(ToolAction toolAction) {
        var self = ((ItemStack) (Object) this);
        if (!EnchantmentPerformer.canPerformAction(self, toolAction)) {
            return false;
        }
        return self.getItem().canPerformAction(self, toolAction);
    }

}
