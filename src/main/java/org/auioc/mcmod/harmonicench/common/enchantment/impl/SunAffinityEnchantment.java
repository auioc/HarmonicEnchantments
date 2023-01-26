package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.game.enchantment.HEnchantmentCategory;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IPlayerEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import org.auioc.mcmod.harmonicench.common.mobeffect.HEMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.LogicalSide;

public class SunAffinityEnchantment extends HEEnchantment implements IItemEnchantment.Elytra, IPlayerEnchantment.Tick {

    public SunAffinityEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            HEnchantmentCategory.ELYTRA,
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

        if (living.isInWaterOrRain()) return false;

        return true;
    }

    @Override
    public void onPlayerTick(int lvl, ItemStack itemStack, EquipmentSlot slot, Player player, Phase phase, LogicalSide side) {
        if (phase == Phase.END && side == LogicalSide.SERVER) {
            if (player.isFallFlying()) {
                player.addEffect(new MobEffectInstance(HEMobEffects.WEIGHTLESSNESS.get(), 5, (Math.min(lvl, this.getMaxLevel()) * 20) - 1, false, true, false));
            } else {
                player.removeEffect(HEMobEffects.WEIGHTLESSNESS.get());
            }
        }
    }

}
