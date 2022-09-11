package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.Random;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class IItemEnchantment {

    public static interface Hurt {

        int onItemHurt(int lvl, ItemStack itemStack, int damage, Random random, ServerPlayer player);

    }

}
