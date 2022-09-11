package org.auioc.mcmod.harmonicench.common.enchantment;

import java.util.function.Supplier;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.BaneOfChampionsEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.BlessingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.EfficacyEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.FreeRidingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.HandinessEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.RapierEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SafeTeleportingEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SiphoningEnchantment;
import org.auioc.mcmod.harmonicench.common.enchantment.impl.SnipingEnchantment;
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

}
