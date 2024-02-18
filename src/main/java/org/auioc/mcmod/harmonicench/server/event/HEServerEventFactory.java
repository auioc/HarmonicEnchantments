package org.auioc.mcmod.harmonicench.server.event;

import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.auioc.mcmod.harmonicench.server.event.impl.ApplyLootEnchantmentBonusCountEvent;
import org.auioc.mcmod.harmonicench.server.event.impl.CatMorningGiftEvent;

public final class HEServerEventFactory {

    private static final IEventBus BUS = NeoForge.EVENT_BUS;

    /**
     * FMLCoreMod: harmonicench.apply_bonus_count.run
     */
    public static int onApplyLootEnchantmentBonusCount(LootContext lootContext, ItemStack itemStack, Enchantment enchantment, int enchantmentLevel) {
        var event = new ApplyLootEnchantmentBonusCountEvent(lootContext, itemStack, enchantment, enchantmentLevel);
        BUS.post(event);
        return event.getEnchantmentLevel();
    }

    /**
     * @see org.auioc.mcmod.harmonicench.mixin.server.MixinCatRelaxOnOwnerGoal#stop
     */
    public static boolean onCatMorningGiftConditionCheck(Cat cat, Player owner) {
        var event = new CatMorningGiftEvent.Check(cat, owner);
        BUS.post(event);
        if (event.isCanceled()) {
            return false;
        }
        return event.check();
    }

}
