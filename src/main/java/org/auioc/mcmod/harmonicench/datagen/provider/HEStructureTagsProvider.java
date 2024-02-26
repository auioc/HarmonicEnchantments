/*
 * Copyright (C) 2024 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.auioc.mcmod.arnicalib.base.reflection.ReflectionUtils;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.data.HETags;

import java.util.concurrent.CompletableFuture;

public class HEStructureTagsProvider extends StructureTagsProvider {

    public HEStructureTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(output, registries, HarmonicEnchantments.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        var dowsingLocatable = tag(HETags.DOWSING_LOCATABLE);
        ReflectionUtils.getFieldValues(BuiltinStructures.class, ResourceKey.class)
            .values()
            .stream()
            // net.minecraft.data.tags.TagsProvider#run
            // java.lang.IllegalArgumentException: Couldn't define tag {} as it is missing following references: minecraft:trial_chambers
            // trial_chambers doesn't exist in 1.20.4
            .filter((k) -> k != BuiltinStructures.TRIAL_CHAMBERS)
            .forEach(dowsingLocatable::add);
    }

}
