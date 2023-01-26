package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.api.enchantment.ILootBonusEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class DeepStudyEnchantment extends HEEnchantment implements ILootBonusEnchantment.ApplyBonusCountFunction {

    public DeepStudyEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            HEnchantmentCategory.PICKAXE,
            EquipmentSlot.MAINHAND,
            3,
            (o) -> o != Enchantments.BLOCK_FORTUNE
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
    public int onApplyLootEnchantmentBonusCount(int lvl, LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        if (enchantment == Enchantments.BLOCK_FORTUNE) {
            var block = lootContext.getParam(LootContextParams.BLOCK_STATE).getBlock();
            if (block instanceof OreBlock && block.defaultMaterialColor() == MaterialColor.DEEPSLATE) {
                return enchantmentLevel + (lvl * 2);
            }
        }
        return enchantmentLevel;
    }

}

