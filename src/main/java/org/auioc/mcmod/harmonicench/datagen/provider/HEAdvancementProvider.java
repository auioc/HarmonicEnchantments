package org.auioc.mcmod.harmonicench.datagen.provider;

import java.util.function.Consumer;
import org.auioc.mcmod.arnicalib.game.datagen.advancement.DataGenAdvancementEntry;
import org.auioc.mcmod.harmonicench.datagen.data.HEAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class HEAdvancementProvider extends AdvancementProvider {

    public HEAdvancementProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, fileHelper);
    }

    @Override
    public String getName() {
        return "HarmonicEnchantmentsAdvancements";
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> writer, ExistingFileHelper fileHelper) {
        HEAdvancements.init();
        // TODO arnicalib ReflectionUtils
        for (var f : HEAdvancements.class.getFields()) {
            if (DataGenAdvancementEntry.class.isAssignableFrom(f.getType())) {
                try {
                    var hea = (DataGenAdvancementEntry) f.get(null);
                    hea.accept(writer, fileHelper);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
