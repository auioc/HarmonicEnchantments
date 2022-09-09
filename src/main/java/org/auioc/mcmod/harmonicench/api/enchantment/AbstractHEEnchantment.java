package org.auioc.mcmod.harmonicench.api.enchantment;

import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractHEEnchantment extends Enchantment implements IMixinEnchantment {

    public AbstractHEEnchantment(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot[] p_44678_) {
        super(p_44676_, p_44677_, p_44678_);
    }

}
