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

package org.auioc.mcmod.harmonicench.datagen.provider;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.auioc.mcmod.arnicalib.base.reflection.ReflectionUtils;
import org.auioc.mcmod.arnicalib.game.datagen.advancement.DataGenAdvancementEntry;
import org.auioc.mcmod.harmonicench.datagen.data.HEAdvancements;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class HEAdvancementProvider extends AdvancementProvider {

    public HEAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(output, registries, fileHelper, List.of(new Generator()));
    }

    private static class Generator implements AdvancementGenerator {

        @Override
        public void generate(Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper fileHelper) {
            HEAdvancements.init();
            ReflectionUtils.getFieldValues(HEAdvancements.class, DataGenAdvancementEntry.class)
                .values()
                .forEach((entry) -> entry.accept(saver, fileHelper));
        }

    }

}
