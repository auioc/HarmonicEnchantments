package org.auioc.mcmod.harmonicench.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.auioc.mcmod.harmonicench.HarmonicEnchantments;
import org.auioc.mcmod.harmonicench.common.damagesource.HEDamageTypes;

import java.util.concurrent.CompletableFuture;

public class HEDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public HEDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(output, registries, HarmonicEnchantments.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags(Provider registries) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(HEDamageTypes.CURSE_OF_REBELLING);
        tag(DamageTypeTags.BYPASSES_EFFECTS).add(HEDamageTypes.CURSE_OF_REBELLING);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(HEDamageTypes.CURSE_OF_REBELLING);
    }

}
