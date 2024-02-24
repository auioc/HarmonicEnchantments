/*
 * Copyright (C) 2024 AUIOC.ORG
 *
 * This file is part of HarmonicLib, a mod made for Minecraft.
 *
 * HarmonicLib is free software: you can redistribute it and/or modify it under
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

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.harmoniclib.advancement.HLCriteriaTriggers;
import org.auioc.mcmod.harmoniclib.client.HLClientInitialization;
import org.auioc.mcmod.harmoniclib.core.HLRegistries;
import org.auioc.mcmod.harmoniclib.event.HLCommonEventHandler;
import org.auioc.mcmod.harmoniclib.event.HLServerEventHandler;

public final class HLInitialization {

    public static void init() {
        modSetup();
        forgeSetup();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            HLClientInitialization.init();
        }
    }

    private static final IEventBus modEventBus = HarmonicLib.getModEventBus();
    private static final IEventBus forgeEventBus = NeoForge.EVENT_BUS;

    private static void forgeSetup() {
        forgeEventBus.register(HLCommonEventHandler.class);
        forgeEventBus.register(HLServerEventHandler.class);
    }

    private static void modSetup() {
        HLCriteriaTriggers.TRIGGERS.register(modEventBus);
        HLRegistries.init(modEventBus);
    }

}
