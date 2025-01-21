/*
 * Copyright (C) 2022-2025 AUIOC.ORG
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

package org.auioc.mcmod.harmonicench.enchantment;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.auioc.mcmod.arnicalib.game.data.TagRecord;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.api.HEEnchantment;
import org.auioc.mcmod.harmonicench.datagen.HEBlockTagsProvider;
import org.auioc.mcmod.harmonicench.datagen.HEEnchantmentTagsProvider;
import org.auioc.mcmod.harmonicench.datagen.HEItemTagsProvider;
import org.auioc.mcmod.harmonicench.enchantment.impl.BaneOfChampionsEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.BluntEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.DeepStudyEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.EfficacyEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.FreeRidingEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.HandinessEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.IceAspectEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.LongEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.LuckOfTheSnowEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.RapierEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.RebellingCurseEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.SafeTeleportingEnchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HEEnchantments {

    public static final ResourceKey<Enchantment> AIM = key("aim"); // TODO
    public static final ResourceKey<Enchantment> BANE_OF_CHAMPIONS = register("bane_of_champions", BaneOfChampionsEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> BLESSING = key("blessing"); // TODO
    public static final ResourceKey<Enchantment> BLUNT = register("blunt", BluntEnchantment::bootstrap); // TODO WIP
    public static final ResourceKey<Enchantment> DEEP_STUDY = register("deep_study", DeepStudyEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> DINING = key("dining"); // TODO
    public static final ResourceKey<Enchantment> EFFICACY = register("efficacy", EfficacyEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> ELECTRIFICATION = key("electrification"); // TODO
    public static final ResourceKey<Enchantment> FORGING = key("forging"); // TODO
    public static final ResourceKey<Enchantment> FREE_RIDING = register("free_riding", FreeRidingEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> HANDINESS = register("handiness", HandinessEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> HARVEST = key("harvest"); // TODO
    public static final ResourceKey<Enchantment> ICE_ASPECT = register("ice_aspect", IceAspectEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> LONG = register("long", LongEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> LUCK_OF_THE_SNOW = register("luck_of_the_snow", LuckOfTheSnowEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> MOB_AFFINITY = key("mob_affinity"); // TODO
    public static final ResourceKey<Enchantment> OBSERVER = key("observer"); // TODO
    public static final ResourceKey<Enchantment> PROFICIENCY = key("proficiency"); // TODO
    public static final ResourceKey<Enchantment> RAPIER = register("rapier", RapierEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> REBELLING_CURSE = register("rebelling_curse", RebellingCurseEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> SACRIFICING_CURSE = key("sacrificing_curse"); // TODO
    public static final ResourceKey<Enchantment> SAFE_TELEPORTING = register("safe_teleporting", SafeTeleportingEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> SNIPING = key("sniping"); // TODO
    public static final ResourceKey<Enchantment> SIPHONING = key("siphoning"); // TODO
    public static final ResourceKey<Enchantment> SUN_AFFINITY = key("sun_affinity"); // TODO
    public static final ResourceKey<Enchantment> TIDE = key("tide"); // TODO

    // ============================================================================================================== //

    private static Map<ResourceKey<Enchantment>, Supplier<HEEnchantment.Bootstrap.Builder>> BOOTSTRAPS;

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, HarmonicEnchantments.id(name));
    }

    private static ResourceKey<Enchantment> register(String name, Supplier<HEEnchantment.Bootstrap.Builder> bootstrap) {
        var key = ResourceKey.create(Registries.ENCHANTMENT, HarmonicEnchantments.id(name));
        if (BOOTSTRAPS == null) BOOTSTRAPS = new HashMap<>();
        BOOTSTRAPS.put(key, bootstrap);
        return key;
    }

    @SuppressWarnings("unchecked")
    public static void register(GatherDataEvent.Server event) {
        var bootstraps = BOOTSTRAPS.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get().build()));

        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();

        var lookupProvider = generator.addProvider(true, new DatapackBuiltinEntriesProvider(
            packOutput, event.getLookupProvider(),
            new RegistrySetBuilder().add(Registries.ENCHANTMENT, (context) -> {
                bootstraps.forEach((key, bootstrap) -> {
                    context.register(key, bootstrap.enchantmentBuilder().apply(key, context).build(key.location()));
                });
            }),
            Set.of(HarmonicEnchantments.MOD_ID)
        )).getRegistryProvider();

        var tags = bootstraps.entrySet().stream().flatMap(e -> e.getValue().tagsToGenerate(e.getKey()).stream()).toList();

        generator.addProvider(true, new HEEnchantmentTagsProvider(packOutput, lookupProvider,
            tags.stream().filter(t -> t.tag().isFor(Registries.ENCHANTMENT)).map(t -> (TagRecord<Enchantment>) t)
        ));

        var blockTags = generator.addProvider(true, new HEBlockTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new HEItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(),
            tags.stream().filter(t -> t.tag().isFor(Registries.ITEM)).map(t -> (TagRecord<Item>) t)
        ));
    }

}
