package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.Map;
import java.util.UUID;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeMod;

public class LongEnchantment extends AbstractHEEnchantment implements IAttributeModifierEnchantment {

    private static final UUID ATTACK_RANGE_UUID = UUID.fromString("2A117CB2-C6AA-15F5-18EE-4B8C74CF8B9F");

    public LongEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.WEAPON,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.SWEEPING_EDGE && o != HEEnchantments.RAPIER.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 15 + (lvl - 1) * 9;
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        return Map.of(
            ForgeMod.ATTACK_RANGE.get(),
            new AttributeModifier(
                ATTACK_RANGE_UUID, this.descriptionId,
                MathUtil.sigma(lvl, 1, (double i) -> 3.0D / (4.0D * i)), AttributeModifier.Operation.ADDITION
            )
        );
    }

}
