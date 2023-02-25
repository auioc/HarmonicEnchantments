package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class ForgingEnchantment extends AbstractHEEnchantment implements IAttributeModifierEnchantment {

    private static final EnchantmentCategory METALLIC_ARMOR = EnchantmentCategory.create(
        "METALLIC_ARMOR",
        (item) -> {
            if (item instanceof ArmorItem arrow && arrow.getMaterial() instanceof ArmorMaterials material) {
                switch (material) {
                    case CHAIN:
                    case IRON:
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

    private static final UUID ARMOR_TOUGHNESS_MODIFIER_UUID = UUID.fromString("26E2A6CB-1EA6-401A-BD70-6ECB85D39969");
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("6EB3BD31-C501-4EFE-BE1E-900A9200A310");

    public ForgingEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            METALLIC_ARMOR,
            new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET},
            (o) -> o != Enchantments.MENDING
                && o != HEEnchantments.DINING.get()
                && o != HEEnchantments.BLESSING.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1;
    }

    @Override
    public int getMaxCost(int lvl) {
        return 41;
    }

    @Nullable
    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        if (itemStack.canEquip(slot, null)) {
            double x = (Math.log((double) itemStack.getBaseRepairCost() + 1) / Math.log(2.0D));
            if (x > 0.0D) {
                double bonus = x * MathUtil.sigma(lvl, 1, (double i) -> 1 / (3 * i));
                return Map.of(
                    Attributes.ARMOR,
                    new AttributeModifier(ARMOR_MODIFIER_UUID, this.descriptionId, bonus, AttributeModifier.Operation.ADDITION),
                    Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(ARMOR_TOUGHNESS_MODIFIER_UUID, this.descriptionId, bonus, AttributeModifier.Operation.ADDITION)
                );
            }
        }
        return null;
    }

}
