package org.auioc.mcmod.harmonicench.datagen;

import org.auioc.mcmod.harmonicench.datagen.provider.HEAdvancementProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HEDataGenerators {

    @SubscribeEvent
    public static void dataGen(GatherDataEvent event) {
        var generator = event.getGenerator();
        var fileHelper = event.getExistingFileHelper();

        generator.addProvider(new HEAdvancementProvider(generator, fileHelper));
    }

}
