package org.auioc.mcmod.harmonicench.api.effect;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IMovementInputMobEffect {

    static float calculateImpulse(boolean a, boolean b, boolean isMovingSlowly) {
        return (a == b) ? 0.0F : ((a ? 1.0F : -1.0F) * (isMovingSlowly ? 0.3F : 1.0F));
    } // TODO arnicalib

    static void calculateImpulse(Input input, boolean isMovingSlowly) {
        input.forwardImpulse = calculateImpulse(input.up, input.down, isMovingSlowly);
        input.leftImpulse = calculateImpulse(input.left, input.right, isMovingSlowly);

    }

    @OnlyIn(Dist.CLIENT)
    void onMovementInputUpdate(int amplifier, Input input, LocalPlayer player);

}
