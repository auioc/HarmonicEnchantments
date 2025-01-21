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
import org.auioc.mcmod.harmonicench.enchantment.impl.AimEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.BaneOfChampionsEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.BlessingEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.BluntEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.DeepStudyEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.DiningEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.EfficacyEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.ElectrificationEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.ForgingEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.FreeRidingEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.HandinessEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.HarvestEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.IceAspectEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.LongEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.LuckOfTheSnowEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.MobAffinityEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.ObserverEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.ProficiencyEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.RapierEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.RebellingCurseEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.SacrificingCurseEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.SafeTeleportingEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.SiphoningEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.SnipingEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.SunAffinityEnchantment;
import org.auioc.mcmod.harmonicench.enchantment.impl.TideEnchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HEEnchantments {

    public static final ResourceKey<Enchantment> AIM = register("aim", AimEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> BANE_OF_CHAMPIONS = register("bane_of_champions", BaneOfChampionsEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> BLESSING = register("blessing", BlessingEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> BLUNT = register("blunt", BluntEnchantment::bootstrap); // TODO WIP
    public static final ResourceKey<Enchantment> DEEP_STUDY = register("deep_study", DeepStudyEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> DINING = register("dining", DiningEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> EFFICACY = register("efficacy", EfficacyEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> ELECTRIFICATION = register("electrification", ElectrificationEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> FORGING = register("forging", ForgingEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> FREE_RIDING = register("free_riding", FreeRidingEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> HANDINESS = register("handiness", HandinessEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> HARVEST = register("harvest", HarvestEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> ICE_ASPECT = register("ice_aspect", IceAspectEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> LONG = register("long", LongEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> LUCK_OF_THE_SNOW = register("luck_of_the_snow", LuckOfTheSnowEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> MOB_AFFINITY = register("mob_affinity", MobAffinityEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> OBSERVER = register("observer", ObserverEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> PROFICIENCY = register("proficiency", ProficiencyEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> RAPIER = register("rapier", RapierEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> REBELLING_CURSE = register("rebelling_curse", RebellingCurseEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> SACRIFICING_CURSE = register("sacrificing_curse", SacrificingCurseEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> SAFE_TELEPORTING = register("safe_teleporting", SafeTeleportingEnchantment::bootstrap);
    public static final ResourceKey<Enchantment> SNIPING = register("sniping", SiphoningEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> SIPHONING = register("siphoning", SnipingEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> SUN_AFFINITY = register("sun_affinity", SunAffinityEnchantment::bootstrap); // TODO
    public static final ResourceKey<Enchantment> TIDE = register("tide", TideEnchantment::bootstrap); // TODO

    // ============================================================================================================== //

    private static Map<ResourceKey<Enchantment>, Supplier<HEEnchantment.Bootstrap.Builder>> BOOTSTRAPS;

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
