package org.auioc.mcmod.harmonicench.api.mixin.common;

import java.util.Set;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public interface IMixinArrow {

    Set<MobEffectInstance> getEffects();

    Potion getPotion();

    void setPotion(Potion potion);

}
