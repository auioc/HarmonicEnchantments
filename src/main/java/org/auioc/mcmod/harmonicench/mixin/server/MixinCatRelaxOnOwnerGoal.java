package org.auioc.mcmod.harmonicench.mixin.server;

import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import org.auioc.mcmod.harmonicench.server.event.HEServerEventFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(targets = "net.minecraft.world.entity.animal.Cat$CatRelaxOnOwnerGoal")
public abstract class MixinCatRelaxOnOwnerGoal {

    @Shadow
    @Final
    private Cat cat;

    @Nullable
    @Shadow
    private Player ownerPlayer;

    @Shadow
    private int onBedTicks;

    @Shadow
    private void giveMorningGift() { }

    // ====================================================================== //

    /**
     * @author WakelessSloth56
     * @reason CatMorningGiftEvent {@link HEServerEventFactory#onCatMorningGiftConditionCheck}
     */
    @Overwrite // TODO better solution than overwrite?
    public void stop() {
        this.cat.setLying(false);
        if (HEServerEventFactory.onCatMorningGiftConditionCheck(cat, ownerPlayer)) {
            this.giveMorningGift();
        }

        this.onBedTicks = 0;
        this.cat.setRelaxStateOne(false);
        this.cat.getNavigation().stop();
    }

}
