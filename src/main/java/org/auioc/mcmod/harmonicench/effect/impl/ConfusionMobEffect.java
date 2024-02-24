/*
 * Copyright (C) 2022-2024 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * HarmonicEnchantments is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.harmonicench.effect.impl;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.auioc.mcmod.arnicalib.game.input.InputUtils;
import org.auioc.mcmod.harmoniclib.effect.IMovementInputMobEffect;

public class ConfusionMobEffect extends MobEffect implements IMovementInputMobEffect {

    public ConfusionMobEffect() {
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
