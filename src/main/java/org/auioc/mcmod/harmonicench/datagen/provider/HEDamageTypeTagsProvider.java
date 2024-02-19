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

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.damagesource.HEDamageTypes;

import java.util.concurrent.CompletableFuture;

public class HEDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public HEDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(output, registries, HarmonicEnchantments.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags(Provider registries) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(HEDamageTypes.CURSE_OF_REBELLING);
        tag(DamageTypeTags.BYPASSES_EFFECTS).add(HEDamageTypes.CURSE_OF_REBELLING);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(HEDamageTypes.CURSE_OF_REBELLING);
    }

}
