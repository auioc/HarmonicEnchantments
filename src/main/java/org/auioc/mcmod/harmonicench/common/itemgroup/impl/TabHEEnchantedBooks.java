package org.auioc.mcmod.harmonicench.common.itemgroup.impl;

import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

public class TabHEEnchantedBooks extends CreativeModeTab {

    public TabHEEnchantedBooks() {
        super("heEnchantedBooks");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.ENCHANTED_BOOK);
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> list) {
        ForgeRegistries.ENCHANTMENTS.getValues()
            .stream()
            .filter((ench) -> ench.getRegistryName().getNamespace().equals(HarmonicEnchantments.MOD_ID))
            .map((ench) -> new EnchantmentInstance(ench, ench.getMaxLevel()))
            .map(EnchantedBookItem::createForEnchantment)
            .forEach(list::add);
    }

}
