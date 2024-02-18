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

package org.auioc.mcmod.harmonicench.common.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.AimEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.BaneOfChampionsEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.BlessingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.BluntEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.CurseOfRebellingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.CurseOfSacrificingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.DeepStudyEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.DiningEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.EfficacyEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.ElectrificationEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.ForgingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.FreeRidingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.HandinessEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.IceAspectEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.LongEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.LuckOfTheSnowEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.MobAffinityEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.ProficiencyEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.RapierEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SafeTeleportingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SiphoningEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SnipingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SunAffinityEnchantment;

import java.util.function.Supplier;

public final class HEEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, HarmonicEnchantments.MOD_ID);

    private static <T extends Enchantment> DeferredHolder<Enchantment, T> register(String _id, Supplier<T> _sup) {
        return ENCHANTMENTS.register(_id, _sup);
    }

    // ============================================================================================================== //

    public static final DeferredHolder<Enchantment, RapierEnchantment> RAPIER = register("rapier", RapierEnchantment::new);
    public static final DeferredHolder<Enchantment, HandinessEnchantment> HANDINESS = register("handiness", HandinessEnchantment::new);
    public static final DeferredHolder<Enchantment, SnipingEnchantment> SNIPING = register("sniping", SnipingEnchantment::new);
    public static final DeferredHolder<Enchantment, SiphoningEnchantment> SIPHONING = register("siphoning", SiphoningEnchantment::new);
    public static final DeferredHolder<Enchantment, BaneOfChampionsEnchantment> BANE_OF_CHAMPIONS = register("bane_of_champions", BaneOfChampionsEnchantment::new);
    public static final DeferredHolder<Enchantment, EfficacyEnchantment> EFFICACY = register("efficacy", EfficacyEnchantment::new);
    public static final DeferredHolder<Enchantment, FreeRidingEnchantment> FREE_RIDING = register("free_riding", FreeRidingEnchantment::new);
    public static final DeferredHolder<Enchantment, SafeTeleportingEnchantment> SAFE_TELEPORTING = register("safe_teleporting", SafeTeleportingEnchantment::new);
    public static final DeferredHolder<Enchantment, BlessingEnchantment> BLESSING = register("blessing", BlessingEnchantment::new);
    public static final DeferredHolder<Enchantment, CurseOfRebellingEnchantment> CURSE_OF_REBELLING = register("curse_of_rebelling", CurseOfRebellingEnchantment::new);
    public static final DeferredHolder<Enchantment, ElectrificationEnchantment> ELECTRIFICATION = register("electrification", ElectrificationEnchantment::new);
    public static final DeferredHolder<Enchantment, LuckOfTheSnowEnchantment> LUCK_OF_THE_SNOW = register("luck_of_the_snow", LuckOfTheSnowEnchantment::new);
    public static final DeferredHolder<Enchantment, DiningEnchantment> DINING = register("dining", DiningEnchantment::new);
    public static final DeferredHolder<Enchantment, IceAspectEnchantment> ICE_ASPECT = register("ice_aspect", IceAspectEnchantment::new);
    public static final DeferredHolder<Enchantment, MobAffinityEnchantment> MOB_AFFINITY = register("mob_affinity", MobAffinityEnchantment::new);
    public static final DeferredHolder<Enchantment, DeepStudyEnchantment> DEEP_STUDY = register("deep_study", DeepStudyEnchantment::new);
    public static final DeferredHolder<Enchantment, ForgingEnchantment> FORGING = register("forging", ForgingEnchantment::new);
    public static final DeferredHolder<Enchantment, ProficiencyEnchantment> PROFICIENCY = register("proficiency", ProficiencyEnchantment::new);
    public static final DeferredHolder<Enchantment, SunAffinityEnchantment> SUN_AFFINITY = register("sun_affinity", SunAffinityEnchantment::new);
    public static final DeferredHolder<Enchantment, CurseOfSacrificingEnchantment> CURSE_OF_SACRIFICING = register("curse_of_sacrificing", CurseOfSacrificingEnchantment::new);
    public static final DeferredHolder<Enchantment, LongEnchantment> LONG = register("long", LongEnchantment::new);
    public static final DeferredHolder<Enchantment, AimEnchantment> AIM = register("aim", AimEnchantment::new);
    public static final DeferredHolder<Enchantment, BluntEnchantment> BLUNT = register("blunt", BluntEnchantment::new);

}
