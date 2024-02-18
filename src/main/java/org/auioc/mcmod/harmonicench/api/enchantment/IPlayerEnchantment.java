package org.auioc.mcmod.harmonicench.api.enchantment;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.event.TickEvent;
import org.apache.commons.lang3.tuple.Pair;


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
