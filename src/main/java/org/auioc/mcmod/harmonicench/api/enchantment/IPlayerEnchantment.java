package org.auioc.mcmod.harmonicench.api.enchantment;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class IPlayerEnchantment {

    public static interface Eat {

        Pair<Integer, Float> onPlayerEat(int lvl, ItemStack itemStack, EquipmentSlot slot, ServerPlayer player, ItemStack foodItemStack, int nutrition, float saturationModifier);

    }

}
