package org.auioc.mcmod.harmonicenc.mixin.common;

import org.auioc.mcmod.harmonicenc.api.mixin.common.IMixinEnchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

@Mixin(value = Enchantment.class)
public class MixinEnchantment implements IMixinEnchantment {

    @Shadow
    @Final
    private EquipmentSlot[] slots;

    @Override
    public EquipmentSlot[] getValidSlots() {
        return slots;
    }

}
