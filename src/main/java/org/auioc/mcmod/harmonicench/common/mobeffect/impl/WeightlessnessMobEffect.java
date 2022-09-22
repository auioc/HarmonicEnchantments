package org.auioc.mcmod.harmonicench.common.mobeffect.impl;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class WeightlessnessMobEffect extends MobEffect {

    public WeightlessnessMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 13565951);
        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "BA6F0F3A-2F07-A1FB-45E8-C7FEEB23B0F3", 0.01D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return (1.0D - (modifier.getAmount() * (double) Math.min(amplifier + 1, 100))) - 1.0D;
    }

}
