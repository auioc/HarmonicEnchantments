/*
 * Copyright (C) 2022-2024 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.api.config;

import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import org.auioc.mcmod.arnicalib.base.holder.ObjectHolder;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class NullableBooleanValue extends ObjectHolder<BooleanValue> {

    private final boolean defaultValue;
    private final Supplier<BooleanValue> valueSupplier;
    private boolean alwaysNull = false;

    public NullableBooleanValue(boolean defaultValue, Supplier<BooleanValue> valueSupplier) {
        super(null);
        this.defaultValue = defaultValue;
        this.valueSupplier = valueSupplier;
    }

    public NullableBooleanValue(Supplier<BooleanValue> valueSupplier) {
        this(true, valueSupplier);
    }

    @Override
    public void set(@Nullable BooleanValue newValue) {
        if (newValue == null) {
            alwaysNull = true;
        } else {
            value = newValue;
            alwaysNull = false;
        }
    }

    public void set() {
        if (!alwaysNull && value == null) {
            set(valueSupplier.get());
        }
    }

    public boolean getValue() {
        set();
        return (alwaysNull) ? defaultValue : value.get();
    }

}
