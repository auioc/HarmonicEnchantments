package org.auioc.mcmod.harmonicench.datagen.provider;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.auioc.mcmod.harmonicench.datagen.data.HEAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class HEAdvancementProvider extends AdvancementProvider {

    public HEAdvancementProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, fileHelper);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> writer, ExistingFileHelper fileHelper) {
        HEAdvancements.init();
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

    // ============================================================================================================== //

    public static record DataGenAdvancementEntry(ResourceLocation id, Advancement.Builder builder) { // TODO arnicalib

        public DataGenAdvancementEntry(ResourceLocation id, UnaryOperator<Advancement.Builder> builder) {
            this(id, builder.apply(Advancement.Builder.advancement()));
        }

        public void accept(Consumer<Advancement> writer, ExistingFileHelper fileHelper) {
            builder.save(writer, id, fileHelper);
        }

    }

}
