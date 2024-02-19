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

package org.auioc.mcmod.harmoniclib;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.Logger;
import org.auioc.mcmod.arnicalib.base.log.LogUtil;
import org.auioc.mcmod.arnicalib.game.mod.IHMod;

@Mod(HarmonicLib.MOD_ID)
public final class HarmonicLib implements IHMod {

    public static final String MOD_ID = "harmoniclib";
    public static final String MOD_NAME = "HarmonicLib";
    public static final Logger LOGGER = LogUtil.getLogger(MOD_NAME);


    private static IEventBus modEventBus;

    public HarmonicLib(IEventBus modEventBus) {
        HarmonicLib.modEventBus = modEventBus;
        HLInitialization.init();
    }

    public static IEventBus getModEventBus() {
        return modEventBus;
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
