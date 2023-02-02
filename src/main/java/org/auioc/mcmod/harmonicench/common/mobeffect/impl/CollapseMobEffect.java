package org.auioc.mcmod.harmonicench.common.mobeffect.impl;

import org.auioc.mcmod.arnicalib.game.input.InputUtils;
import org.auioc.mcmod.harmonicench.api.effect.IMovementInputMobEffect;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CollapseMobEffect extends MobEffect implements IMovementInputMobEffect {

    public CollapseMobEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF800080);
    }

    @Override
    public void onMovementInputUpdate(int amplifier, Input input, LocalPlayer player) {
        boolean _t = input.jumping;
        input.jumping = input.shiftKeyDown;
        input.shiftKeyDown = _t;
        input.up = !input.up;
        input.down = !input.down;
        input.left = !input.left;
        input.right = !input.right;
        InputUtils.calculateImpulse(input, player.isMovingSlowly());
    }

}
