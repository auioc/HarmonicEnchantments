package org.auioc.mcmod.harmonicench.api.enchantment;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class IBlockEnchantment {

    public static interface BreakSpeed {

        float getBreakSpeed(int lvl, ItemStack itemStack, Player player, BlockState blockState, Optional<BlockPos> blockPos, float speed);

    }

    public static interface Break {

        void onBlockBreak(int lvl, ItemStack itemStack, Player player, BlockState blockState, BlockPos blockPos);

    }

}
