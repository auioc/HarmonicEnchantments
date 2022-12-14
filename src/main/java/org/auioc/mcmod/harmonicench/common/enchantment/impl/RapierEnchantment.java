package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.Map;
import java.util.UUID;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IToolActionControllerEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class RapierEnchantment extends HEEnchantment implements IAttributeModifierEnchantment, IToolActionControllerEnchantment {

    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("DD970DD3-E85C-C575-F1E2-4708A674A99C");

    public RapierEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.WEAPON,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.SWEEPING_EDGE
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
    public boolean canPerformAction(ToolAction toolAction) {
        return !toolAction.equals(ToolActions.SWORD_SWEEP);
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        return Map.of(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(
                ATTACK_SPEED_UUID, this.descriptionId,
                MathUtil.sigma(lvl, 0, (double i) -> 1 / (i + 9.0D)), AttributeModifier.Operation.ADDITION
            )
        );
    }

}
