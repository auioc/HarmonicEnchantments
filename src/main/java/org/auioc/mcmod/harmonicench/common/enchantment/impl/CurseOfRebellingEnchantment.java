package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.Random;
import org.auioc.mcmod.arnicalib.utils.game.TextUtils;
import org.auioc.mcmod.arnicalib.utils.java.RandomUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.HEEnchantments;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CurseOfRebellingEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Hurt {

    public CurseOfRebellingEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public int getMinCost(int lvl) {
        return 10 + 20 * (lvl - 1);
    }

    @Override
    public int getMaxCost(int lvl) {
        return super.getMinCost(lvl) + 50;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other) && other != HEEnchantments.FREE_RIDING.get();
    }

    @Override
    public int onItemHurt(int lvl, ItemStack itemStack, int damage, Random random, ServerPlayer player) {
        if (damage > 0) {
            if (RandomUtils.percentageChance(1, player.getRandom())) {
                player.hurt(new CurseOfRebellingDamageSource(itemStack), damage * 4);
            }
        }
        return damage;
    }


    public static class CurseOfRebellingDamageSource extends DamageSource {

        private final ItemStack betrayedItem;

        public CurseOfRebellingDamageSource(ItemStack itemStack) {
            super("curseOfRebelling");
            this.bypassArmor().bypassMagic().isMagic();
            this.betrayedItem = itemStack;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity living) {
            return TextUtils.translatable("death.attack.curseOfRebelling", living.getDisplayName(), this.betrayedItem.getDisplayName());
        }

    }

}
