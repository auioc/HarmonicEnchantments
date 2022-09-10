package org.auioc.mcmod.harmonicench.api.enchantment;

import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractHEEnchantment extends Enchantment implements IMixinEnchantment {

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] validSlots) {
        super(rarity, category, validSlots);
    }

    public AbstractHEEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot validSlot) {
        this(rarity, category, new EquipmentSlot[] {validSlot});
    }

}
