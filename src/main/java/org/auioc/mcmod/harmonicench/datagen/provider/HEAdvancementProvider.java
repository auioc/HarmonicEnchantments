package org.auioc.mcmod.harmonicench.datagen.provider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.auioc.mcmod.arnicalib.base.reflection.ReflectionUtils;
import org.auioc.mcmod.arnicalib.game.datagen.advancement.DataGenAdvancementEntry;
import org.auioc.mcmod.harmonicench.datagen.data.HEAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

public class HEAdvancementProvider extends ForgeAdvancementProvider {

    public HEAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(output, registries, fileHelper, List.of(new Generator()));
    }

    private static class Generator implements AdvancementGenerator {

        @Override
        public void generate(Provider registries, Consumer<Advancement> saver, ExistingFileHelper fileHelper) {
            HEAdvancements.init();
            ReflectionUtils.getFieldValues(HEAdvancements.class, DataGenAdvancementEntry.class)
                .values()
                .forEach((entry) -> entry.accept(saver, fileHelper));
        }

    }

}
