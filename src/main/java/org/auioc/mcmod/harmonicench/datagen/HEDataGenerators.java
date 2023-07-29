package org.auioc.mcmod.harmonicench.datagen;

import java.util.Set;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.common.damagesource.HEDamageTypes;
import org.auioc.mcmod.harmonicench.datagen.provider.HEAdvancementProvider;
import org.auioc.mcmod.harmonicench.datagen.provider.HEDamageTypeTagsProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class HEDataGenerators {

    private static final RegistrySetBuilder BUILTIN_ENTRIES_BUILDER = new RegistrySetBuilder()
        .add(Registries.DAMAGE_TYPE, HEDamageTypes::bootstrap);

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void dataGen(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var fileHelper = event.getExistingFileHelper();
        boolean run = event.includeServer();
        var registries = generator.addProvider(run, new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), BUILTIN_ENTRIES_BUILDER, Set.of(HarmonicEnchantments.MOD_ID)))
            .getRegistryProvider();

        generator.addProvider(run, new HEAdvancementProvider(output, registries, fileHelper));
        generator.addProvider(run, new HEDamageTypeTagsProvider(output, registries, fileHelper));
    }

}
