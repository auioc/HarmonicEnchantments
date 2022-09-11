package org.auioc.mcmod.harmonicench.server.event;

import java.util.Random;
import javax.annotation.Nullable;
import org.auioc.mcmod.harmonicench.server.event.impl.ItemHurtEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public final class HEServerEventFactory {

    private static final IEventBus BUS = MinecraftForge.EVENT_BUS;

    public static int onItemHurt(ItemStack itemStack, int damage, Random random, @Nullable ServerPlayer player) {
        var event = new ItemHurtEvent(itemStack, damage, random, player);
        BUS.post(event);
        return event.getDamage();
    }

    public static void hurt(int p_41630_, Random p_41631_, @Nullable ServerPlayer p_41632_) {
        p_41630_ = onItemHurt(ItemStack.EMPTY, p_41630_, p_41631_, p_41632_);
    }
}
