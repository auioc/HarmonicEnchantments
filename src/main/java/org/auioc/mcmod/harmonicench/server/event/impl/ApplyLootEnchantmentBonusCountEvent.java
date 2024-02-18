package org.auioc.mcmod.harmonicench.server.event.impl;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.bus.api.Event;

public class ApplyLootEnchantmentBonusCountEvent extends Event {

    private final LootContext lootContext;
    private final ItemStack itemStack;
    private final Enchantment enchantment;
    private int enchantmentLevel;

    public ApplyLootEnchantmentBonusCountEvent(LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        this.lootContext = lootContext;
        this.itemStack = itemStack;
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
    }

    public LootContext getLootContext() {
        return this.lootContext;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public int getEnchantmentLevel() {
        return this.enchantmentLevel;
    }

    public void setEnchantmentLevel(int enchantmentLevel) {
        this.enchantmentLevel = enchantmentLevel;
    }

}
