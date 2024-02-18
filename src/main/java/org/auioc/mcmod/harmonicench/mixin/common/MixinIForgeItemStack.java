package org.auioc.mcmod.harmonicench.mixin.common;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.auioc.mcmod.harmonicench.utils.EnchantmentPerformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = IItemStackExtension.class, remap = false)
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
