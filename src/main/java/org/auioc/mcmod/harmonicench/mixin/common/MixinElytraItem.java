package org.auioc.mcmod.harmonicench.mixin.common;

import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;

@Mixin(value = ElytraItem.class)
public class MixinElytraItem {

    /**
     * @author WakelessSloth56
     * @reason {@link org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment.Elytra#canElytraFly}
     */
    @Overwrite(remap = false)
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return EnchantmentHelper.canElytraFly(stack, entity) && ElytraItem.isFlyEnabled(stack);
    }

}
