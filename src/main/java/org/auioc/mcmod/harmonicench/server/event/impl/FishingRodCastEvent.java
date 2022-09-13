package org.auioc.mcmod.harmonicench.server.event.impl;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class FishingRodCastEvent extends PlayerEvent {

    private final Level level;
    private final ItemStack fishingRod;

    public FishingRodCastEvent(Player player, Level level, ItemStack fishingRod) {
        super(player);
        this.level = level;
        this.fishingRod = fishingRod;
    }

    public Level getLevel() {
        return this.level;
    }

    public ItemStack getFishingRod() {
        return this.fishingRod;
    }

    public static class Pre extends FishingRodCastEvent {

        private int speedBonus;
        private int luckBonus;

        public Pre(Player player, Level level, ItemStack fishingRod, int speedBonus, int luckBonus) {
            super(player, level, fishingRod);
            this.speedBonus = speedBonus;
            this.luckBonus = luckBonus;
        }

        public int getSpeedBonus() {
            return this.speedBonus;
        }

        public void setSpeedBonus(int speedBonus) {
            this.speedBonus = speedBonus;
        }

        public int getLuckBonus() {
            return this.luckBonus;
        }

        public void setLuckBonus(int luckBonus) {
            this.luckBonus = luckBonus;
        }

    }

}
