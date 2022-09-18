package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.HashSet;
import org.auioc.mcmod.arnicalib.api.game.entity.ITippedArrow;
import org.auioc.mcmod.arnicalib.api.mixin.common.IMixinMobEffectInstance;
import org.auioc.mcmod.harmonicench.api.enchantment.AbstractHEEnchantment;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinSpectralArrow;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class EfficacyEnchantment extends AbstractHEEnchantment implements IProjectileEnchantment.TippedArrow, IProjectileEnchantment.SpectralArrow {

    public EfficacyEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND, 4);
    }

    @Override
    public int getMinCost(int lvl) {
        return 12 + (lvl - 1) * 20;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 25;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other)
            && other != Enchantments.POWER_ARROWS
            && other != Enchantments.FLAMING_ARROWS
            && other != Enchantments.MULTISHOT
            && other != Enchantments.PIERCING;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return itemStack.getItem() instanceof CrossbowItem ? true : super.canEnchant(itemStack);
    }

    @Override
    public void handleTippedArrow(int lvl, Arrow arrow, ITippedArrow potionArrow) {
        var effects = potionArrow.getEffects();

        for (var effectI : potionArrow.getPotion().getEffects()) {
            effects.add(
                new MobEffectInstance(
                    effectI.getEffect(),
                    Math.max(effectI.getDuration() / 8, 1), effectI.getAmplifier(),
                    effectI.isAmbient(), effectI.isVisible()
                )
            );
        }
        potionArrow.setPotion(Potions.EMPTY);

        double amplifierBonus = 0.0D;
        for (int k = 1, n = lvl + 1; k < n; k++) amplifierBonus += 1.0D / ((double) k);

        var newEffects = new HashSet<MobEffectInstance>();
        for (var _old : effects) {
            int newAmplifier = _old.getAmplifier() + ((int) amplifierBonus);
            int newDuration = (_old.getEffect().isInstantenous())
                ? _old.getDuration()
                : addDurationBonus(lvl, _old.getDuration());
            var _new = new MobEffectInstance(
                _old.getEffect(),
                newDuration, newAmplifier,
                _old.isAmbient(), _old.isVisible(), _old.showIcon(),
                ((IMixinMobEffectInstance) _old).getHiddenEffect()
            );
            _new.setCurativeItems(_old.getCurativeItems());
            newEffects.add(_new);
        }
        effects.clear();
        effects.addAll(newEffects);
        return;
    }

    @Override
    public void handleSpectralArrow(int lvl, SpectralArrow spectralArrow) {
        var _spectralArrow = (IMixinSpectralArrow) spectralArrow;
        _spectralArrow.setDuration(addDurationBonus(lvl, _spectralArrow.getDuration()));
    }

    private static int addDurationBonus(int lvl, int duration) {
        return (int) (duration * (1 + ((lvl + 1.0D) * 0.1D)));
    }

}
