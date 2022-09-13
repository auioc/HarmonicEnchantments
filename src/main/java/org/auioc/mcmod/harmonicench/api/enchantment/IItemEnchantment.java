package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.Random;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IItemEnchantment {

    public static interface Hurt {

        int onItemHurt(int lvl, ItemStack itemStack, int damage, Random random, ServerPlayer player);

    }

    public static interface Protection {

        int getDamageProtection(int lvl, ItemStack itemStack, DamageSource source);

    }

    public static class Tick {

        public static interface Selected {

            void onSelectedTick(int lvl, ItemStack itemStack, Player player, Level level);

        }

    }

}
