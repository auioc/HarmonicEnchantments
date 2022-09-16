package org.auioc.mcmod.harmonicench.server.event;

import java.util.Random;
import javax.annotation.Nullable;
import org.auioc.mcmod.harmonicench.server.event.impl.CatMorningGiftChanceEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.FishingRodCastEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.ItemHurtEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.PiglinStanceEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public final class HEServerEventFactory {

    private static final IEventBus BUS = MinecraftForge.EVENT_BUS;

    public static int onItemHurt(ItemStack itemStack, int damage, Random random, @Nullable ServerPlayer player) {
        var event = new ItemHurtEvent(itemStack, damage, random, player);
        BUS.post(event);
        return event.getDamage();
    }

    public static FishingRodCastEvent.Pre preFishingRodCast(Player player, Level level, ItemStack fishingRod, int speedBonus, int luckBonus) {
        var event = new FishingRodCastEvent.Pre((ServerPlayer) player, (ServerLevel) level, fishingRod, speedBonus, luckBonus);
        BUS.post(event);
        return event;
    }

    public static PiglinStanceEvent.Stance firePiglinStanceEvent(LivingEntity target) {
        var event = new PiglinStanceEvent(target);
        BUS.post(event);
        return event.getStance();
    }

    public static double fireCatMorningGiftChanceEvent(Cat cat, Player ownerPlayer) {
        var event = new CatMorningGiftChanceEvent(cat, ownerPlayer);
        BUS.post(event);
        return event.getChance();
    }

}
