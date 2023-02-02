package org.auioc.mcmod.harmonicench.common.enchantment.impl;

import java.util.HashSet;
import java.util.function.DoubleUnaryOperator;
import org.auioc.mcmod.arnicalib.base.math.MathUtil;
import org.auioc.mcmod.arnicalib.game.entity.projectile.ITippedArrow;
import org.auioc.mcmod.arnicalib.mod.mixin.common.MixinAccessorMobEffectInstance;
import org.auioc.mcmod.harmonicench.api.enchantment.IProjectileEnchantment;
import org.auioc.mcmod.harmonicench.api.mixin.common.IMixinSpectralArrow;
import org.auioc.mcmod.harmonicench.common.enchantment.base.HEEnchantment;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class EfficacyEnchantment extends HEEnchantment implements IProjectileEnchantment.TippedArrow, IProjectileEnchantment.SpectralArrow, IProjectileEnchantment.FireworkRocket {

    public EfficacyEnchantment() {
        super(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.BOW,
            EquipmentSlot.MAINHAND,
            4,
            (o) -> o != Enchantments.POWER_ARROWS
                && o != Enchantments.FLAMING_ARROWS
                && o != Enchantments.MULTISHOT
                && o != Enchantments.PIERCING
        );
    }

    @Override
    public int getMinCost(int lvl) {
        return 1 + (lvl - 1) * 10;
    }

    @Override
    public int getMaxCost(int lvl) {
        return this.getMinCost(lvl) + 15;
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
                ((MixinAccessorMobEffectInstance) _old).getHiddenEffect()
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

    @Override
    public void handleFireworkRocket(int lvl, FireworkRocketEntity fireworkRocket) {}

    @Override
    public float onFireworkRocketExplode(int lvl, LivingEntity target, FireworkRocketEntity projectile, LivingEntity owner, float amount) {
        double ynn = MathUtil.sigma(lvl, 1, (DoubleUnaryOperator) (i) -> 1.0D / i);
        int luckAmplifier = ((int) ynn) - 1;
        int luckDuration = ((int) (((10.0D + ((double) lvl)) / 10.0D) * 37.5D)) * 20;
        owner.addEffect(new MobEffectInstance(MobEffects.LUCK, luckDuration, luckAmplifier));
        return amount * (1.0F + ((float) ynn));
    }

}
