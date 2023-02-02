package org.auioc.mcmod.harmonicench.api.effect;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IMovementInputMobEffect {

    @OnlyIn(Dist.CLIENT)
    void onMovementInputUpdate(int amplifier, Input input, LocalPlayer player);

}
