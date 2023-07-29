package org.auioc.mcmod.harmonicench.server.event;

import org.auioc.mcmod.harmonicench.server.event.impl.ApplyLootEnchantmentBonusCountEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public final class HEServerEventFactory {

    private static final IEventBus BUS = MinecraftForge.EVENT_BUS;

    /**
     * FMLCoreMod: harmonicench.apply_bonus_count.run
     */
    public static int onApplyLootEnchantmentBonusCount(LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        var event = new ApplyLootEnchantmentBonusCountEvent(lootContext, itemStack, enchantment, enchantmentLevel);
        BUS.post(event);
        return event.getEnchantmentLevel();
    }

}
