package org.auioc.mcmod.harmonicench.api.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;

public class ILootBonusEnchantment {

    public static interface ApplyBonusCountFunction {

        int onApplyLootEnchantmentBonusCount(int lvl, LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel);

    }

}
