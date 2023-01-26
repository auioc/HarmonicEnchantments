package org.auioc.mcmod.harmonicench.common.enchantment;

import java.util.function.Supplier;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HEEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, HarmonicEnchantments.MOD_ID);

    private static RegistryObject<Enchantment> register(String id, Supplier<? extends Enchantment> sup) {
        return ENCHANTMENTS.register(id, sup);
    }

    public static final RegistryObject<Enchantment> RAPIER = register("rapier", RapierEnchantment::new);
    public static final RegistryObject<Enchantment> HANDINESS = register("handiness", HandinessEnchantment::new);
    public static final RegistryObject<Enchantment> SNIPING = register("sniping", SnipingEnchantment::new);
    public static final RegistryObject<Enchantment> SIPHONING = register("siphoning", SiphoningEnchantment::new);
    public static final RegistryObject<Enchantment> BANE_OF_CHAMPIONS = register("bane_of_champions", BaneOfChampionsEnchantment::new);
    public static final RegistryObject<Enchantment> EFFICACY = register("efficacy", EfficacyEnchantment::new);
    public static final RegistryObject<Enchantment> FREE_RIDING = register("free_riding", FreeRidingEnchantment::new);
    public static final RegistryObject<Enchantment> SAFE_TELEPORTING = register("safe_teleporting", SafeTeleportingEnchantment::new);
    public static final RegistryObject<Enchantment> BLESSING = register("blessing", BlessingEnchantment::new);
    public static final RegistryObject<Enchantment> CURSE_OF_REBELLING = register("curse_of_rebelling", CurseOfRebellingEnchantment::new);
    public static final RegistryObject<Enchantment> ELECTRIFICATION = register("electrification", ElectrificationEnchantment::new);
    public static final RegistryObject<Enchantment> LUCK_OF_THE_SNOW = register("luck_of_the_snow", LuckOfTheSnowEnchantment::new);
    public static final RegistryObject<Enchantment> DINING = register("dining", DiningEnchantment::new);
    public static final RegistryObject<Enchantment> ICE_ASPECT = register("ice_aspect", IceAspectEnchantment::new);
    public static final RegistryObject<Enchantment> MOB_AFFINITY = register("mob_affinity", MobAffinityEnchantment::new);
    public static final RegistryObject<Enchantment> DEEP_STUDY = register("deep_study", DeepStudyEnchantment::new);
    public static final RegistryObject<Enchantment> FORGING = register("forging", ForgingEnchantment::new);
    public static final RegistryObject<Enchantment> PROFICIENCY = register("proficiency", ProficiencyEnchantment::new);
    public static final RegistryObject<Enchantment> SUN_AFFINITY = register("sun_affinity", SunAffinityEnchantment::new);
    public static final RegistryObject<Enchantment> CURSE_OF_SACRIFICING = register("curse_of_sacrificing", CurseOfSacrificingEnchantment::new);
    public static final RegistryObject<Enchantment> LONG = register("long", LongEnchantment::new);
    public static final RegistryObject<Enchantment> AIM = register("aim", AimEnchantment::new);
    public static final RegistryObject<Enchantment> BLUNT = register("blunt", BluntEnchantment::new);

}
