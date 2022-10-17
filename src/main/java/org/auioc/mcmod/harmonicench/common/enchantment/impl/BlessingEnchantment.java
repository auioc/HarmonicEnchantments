package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import org.auioc.mcmod.harmonicench.utils.EnchantmentHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class BlessingEnchantment extends HEEnchantment implements IItemEnchantment.Protection {

    private static final EnchantmentCategory BLESSABLE_ARMOR = EnchantmentCategory.create(
        "BLESSABLE_ARMOR",
        (item) -> {
            if (item instanceof ArmorItem arrow && arrow.getMaterial() instanceof ArmorMaterials material) {
                switch (material) {
                    case LEATHER:
                    case GOLD:
                    case NETHERITE: {
                        return true;
                    }
                    default: {
                        break;
                    }
                }
            }
            return false;
        }
    );

    public BlessingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            BLESSABLE_ARMOR,
            new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET},
            (o) -> o != Enchantments.MENDING && o != HEEnchantments.FORGING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 51;
    }

    @Override
    public int getDamageProtection(int lvl, ItemStack itemStack, DamageSource source) {
        if (source.isMagic()) {
            int N = EnchantmentHelper.getEnchantments(itemStack).values().stream().mapToInt((i) -> i).sum();
            double l = MathUtil.sigma(N, 1, (double i) -> 3.0D / (2.0D * i - 1.0D));
            double r = MathUtil.sigma(lvl, 1, (double i) -> 1.0D / (3.0D * i - 2.0D));
            return (int) (l * r);
        }
        return 0;
    }

}
