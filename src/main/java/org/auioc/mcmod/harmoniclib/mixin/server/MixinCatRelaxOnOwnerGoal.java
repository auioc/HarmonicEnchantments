/*
 * Copyright (C) 2024 AUIOC.ORG
 *
 * This file is part of HarmonicEnchantments, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
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

package org.auioc.mcmod.harmoniclib.mixin.server;

import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import org.auioc.mcmod.harmoniclib.event.HLServerEventFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(targets = "net.minecraft.world.entity.animal.Cat$CatRelaxOnOwnerGoal")
public abstract class MixinCatRelaxOnOwnerGoal {

    @Shadow
    @Final
    private Cat cat;

    @Nullable
    @Shadow
    private Player ownerPlayer;

    @Shadow
    private int onBedTicks;

    @Shadow
    private void giveMorningGift() { }

    // ====================================================================== //

    /**
     * @author WakelessSloth56
     * @reason CatMorningGiftEvent {@link HLServerEventFactory#onCatMorningGiftConditionCheck}
     */
    @Overwrite // TODO better solution than overwrite?
    public void stop() {
        this.cat.setLying(false);
        if (HLServerEventFactory.onCatMorningGiftConditionCheck(cat, ownerPlayer)) {
            this.giveMorningGift();
        }

        this.onBedTicks = 0;
        this.cat.setRelaxStateOne(false);
        this.cat.getNavigation().stop();
    }

}
