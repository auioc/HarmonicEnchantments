package org.auioc.mcmod.harmonicench.api.enchantment;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

public class IPlayerEnchantment {

    public static interface Eat {

        Pair<Integer, Float> onPlayerEat(int lvl, ItemStack itemStack, EquipmentSlot slot, ServerPlayer player, ItemStack foodItemStack, int nutrition, float saturationModifier);

    }

    public static interface Tick {

        void onPlayerTick(int lvl, ItemStack itemStack, EquipmentSlot slot, Player player, TickEvent.Phase phase, LogicalSide side);

    }

    public static interface CriticalHit {

        float onCriticalHit(int lvl, ItemStack itemStack, Player player, Entity target, float damageModifier);

    }

}
