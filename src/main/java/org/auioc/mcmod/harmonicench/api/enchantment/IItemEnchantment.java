package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IItemEnchantment {

    public static interface Hurt {

        int onItemHurt(int lvl, ItemStack itemStack, int damage, Random random, ServerPlayer player);

    }

    public static interface Protection {

        int getDamageProtection(int lvl, ItemStack itemStack, DamageSource source);

    }

    public static interface Elytra {

        boolean canElytraFly(int lvl, ItemStack elytra, LivingEntity living);

    }

    public static interface FishingRod {

        Pair<Integer, Integer> preFishingRodCast(int lvl, ItemStack fishingRod, ServerPlayer player, int speedBonus, int luckBonus);

    }

    public static class Tick {

        public static interface Inventory {

            void onInventoryTick(int lvl, ItemStack itemStack, Player player, Level level, int index, boolean selected);

        }

    }

    public static interface Tooltip {

        @OnlyIn(Dist.CLIENT)
        void onItemTooltip(int lvl, @Nonnull ItemStack itemStack, @Nullable Player player, List<Component> lines, TooltipFlag flags);

    }

}
