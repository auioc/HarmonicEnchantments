package org.auioc.mcmod.harmonicenc.common.enchantment;

import java.util.function.Supplier;
import org.auioc.mcmod.harmonicenc.HarmonicEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class HEEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, HarmonicEnchantments.MOD_ID);

    private static RegistryObject<Enchantment> register(String id, Supplier<? extends Enchantment> sup) {
        return ENCHANTMENTS.register(id, sup);
    }

}
