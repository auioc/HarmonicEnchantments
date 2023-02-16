package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.Map;
import java.util.UUID;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.arnicalib.game.tag.HItemTags;
import org.auioc.mcmod.harmonicench.api.enchantment.IAttributeModifierEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import org.auioc.mcmod.harmonicench.common.mobeffect.HEMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class BluntEnchantment extends HEEnchantment implements IAttributeModifierEnchantment, IPlayerEnchantment.CriticalHit {

    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("F839F42C-4B26-6F66-7025-1EF3294EED97");

    public BluntEnchantment() {
        super(
            Enchantment.Rarity.UNCOMMON,
            HEnchantmentCategory.AXE,
            EquipmentSlot.MAINHAND,
            5,
            (o) -> o != Enchantments.SHARPNESS
                && o != Enchantments.SMITE
                && o != Enchantments.BANE_OF_ARTHROPODS
                && o != Enchantments.BLOCK_EFFICIENCY
                && o != HEEnchantments.BANE_OF_CHAMPIONS.get()
                && o != HEEnchantments.RAPIER.get()
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 5 + (lvl - 1) * 8;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 20;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        var item = itemStack.getItem();
        return (item instanceof SwordItem || isBrick(itemStack)) ? true : super.canEnchant(itemStack);
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifier(int lvl, EquipmentSlot slot, ItemStack itemStack) {
        return Map.of(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(
                ATTACK_SPEED_UUID, this.descriptionId,
                isBrick(itemStack) ? -0.95D : -0.25D, AttributeModifier.Operation.MULTIPLY_TOTAL
            )
        );
    }

    @Override
    public float onCriticalHit(int lvl, ItemStack itemStack, Player player, Entity target, float damageModifier) {
        if (isBrick(itemStack) && target instanceof Player targetPlayer) {
            targetPlayer.addEffect(new MobEffectInstance(HEMobEffects.CONFUSION.get(), (int) MathUtil.sigma(lvl, 1, (double i) -> 5.0D / i) * 20));
        }
        return damageModifier + (0.5F * lvl);
    }

    // ====================================================================== //

    private static boolean isBrick(ItemStack itemStack) {
        return itemStack.is(HItemTags.BRICKS);
    }

}
