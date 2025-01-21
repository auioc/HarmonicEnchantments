/*
 * Copyright (C) 2025 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.world.item.enchantment.Enchantment;
import org.auioc.mcmod.arnicalib.game.data.TagRecord;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class HEEnchantmentTagsProvider extends EnchantmentTagsProvider {

    private final Stream<TagRecord<Enchantment>> tags;

    public HEEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, Stream<TagRecord<Enchantment>> tags) {
        super(output, lookupProvider, HarmonicEnchantments.MOD_ID);
        this.tags = tags;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tags.forEach(t -> t.build(this::tag));
    }

}
