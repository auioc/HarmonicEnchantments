package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.common.mobeffect.HEMobEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SunAffinityEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Elytra, IPlayerEnchantment.Tick.Server {

    private static final EnchantmentCategory ELYTRA = EnchantmentCategory.create("ELYTRA", (item) -> item instanceof ElytraItem);

    public SunAffinityEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            ELYTRA,
            EquipmentSlot.CHEST,
            3
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 10 * lvl;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 30;
    }

    @Override
    public boolean canElytraFly(int lvl, ItemStack elytra, LivingEntity living) {
        var level = living.getLevel();

        if (!level.dimensionType().natural()) return false;

        int dayTime = (int) (level.getDayTime() % 24000L);
        if (!(dayTime >= 0 && dayTime < 12000)) return false;

        if (level.isRaining()) return false;

        return true;
    }

    @Override
    public void onPlayerServerTick(int lvl, ItemStack itemStack, EquipmentSlot slot, ServerPlayer player) {
        if (player.isFallFlying()) {
            player.addEffect(new MobEffectInstance(HEMobEffects.WEIGHTLESSNESS.get(), 5, (Math.min(lvl, this.getMaxLevel()) * 20) - 1, false, true, false));
        } else {
            player.removeEffect(HEMobEffects.WEIGHTLESSNESS.get());
        }
    }

}
