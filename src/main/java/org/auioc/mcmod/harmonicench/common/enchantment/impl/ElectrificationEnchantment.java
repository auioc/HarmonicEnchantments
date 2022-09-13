package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import org.auioc.mcmod.arnicalib.utils.java.RandomUtils;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IItemEnchantment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ElectrificationEnchantment extends AbstractHEEnchantment implements IItemEnchantment.Tick.Selected {

    public ElectrificationEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.TRIDENT, EquipmentSlot.MAINHAND, 5);
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other)
            && other != Enchantments.RIPTIDE
            && other != Enchantments.IMPALING;
    }

    @Override
    public void onSelectedTick(int lvl, ItemStack itemStack, Player player, Level level) {
        if (level.isClientSide) return;

        if (player.tickCount % 20 != 0) return;

        var serverLevel = (ServerLevel) level;

        int chance;
        if (serverLevel.isThundering()) {
            chance = 5;
        } else if (serverLevel.isRaining()) {
            chance = 1;
        } else {
            return;
        }
        if (RandomUtils.percentageChance(100 - chance, player.getRandom())) return;

        var lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
        lightning.setPos(player.position());
        lightning.setVisualOnly(true);
        serverLevel.addFreshEntity(lightning);

        double effectDuration = 0.0D;
        double effectLevel = 0.0D;
        for (int k = 1, n = lvl + 1; k < n; k++) {
            effectDuration += 15.0D * (1.0D / ((double) k));
            effectLevel += 5.0D / (2.0D * ((double) k) + 1.0D);
        }
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, ((int) effectDuration) * 20, ((int) effectLevel) - 1));
    }

}