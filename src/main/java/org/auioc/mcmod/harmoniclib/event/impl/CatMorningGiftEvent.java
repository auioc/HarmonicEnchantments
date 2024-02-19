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

package org.auioc.mcmod.harmoniclib.event.impl;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

import java.util.LinkedHashMap;
import java.util.function.BiPredicate;

public class CatMorningGiftEvent extends LivingEvent {

    protected final Cat cat;
    protected final Player owner;

    public CatMorningGiftEvent(Cat cat, Player owner) {
        super(cat);
        this.cat = cat;
        this.owner = owner;
    }

    public Cat getCat() {
        return this.cat;
    }

    public Player getOwner() {
        return this.owner;
    }

    // ============================================================================================================== //

    public static class Check extends CatMorningGiftEvent implements ICancellableEvent { // TODO better conditions impl?

        public static final LinkedHashMap<ResourceLocation, BiPredicate<Cat, Player>> DEFAULT_CONDITIONS = new LinkedHashMap<>(3) {
            {
                put(new ResourceLocation("sleeper_timer"), (cat, owner) -> owner.getSleepTimer() >= 100);
                put(new ResourceLocation("time_of_day"), (cat, owner) -> {
                    double time = cat.level().getTimeOfDay(1.0F);
                    return time > 0.77D && time < 0.8D;
                });
                put(new ResourceLocation("random"), (cat, owner) -> cat.level().getRandom().nextFloat() < 0.7D);
            }
        };

        private final LinkedHashMap<ResourceLocation, BiPredicate<Cat, Player>> conditions = new LinkedHashMap<>();

        public Check(Cat cat, Player owner) {
            super(cat, owner);
            this.conditions.putAll(DEFAULT_CONDITIONS);
        }

        public LinkedHashMap<ResourceLocation, BiPredicate<Cat, Player>> getConditions() {
            return this.conditions;
        }

        public boolean check() {
            for (var predicate : this.conditions.values()) {
                boolean result = predicate.test(this.cat, this.owner);
                if (!result) {
                    return false;
                }
            }
            return true;
        }

    }

}
