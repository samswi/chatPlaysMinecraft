package org.samswi.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.PlayerInput;
import org.samswi.client.TwitchListener;
import org.samswi.client.twvsytClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.samswi.client.CInputs;

@Mixin(KeyboardInput.class)
public abstract class InputMixin extends Input {
    @Shadow
    private static float getMovementMultiplier(boolean positive, boolean negative) {
        return 0;
    }

    private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "tick", at = @At("TAIL"))
    public void modifyInput(CallbackInfo ci) {
        if (CInputs.enabled && twvsytClient.chatListenerEnabled && !(client.currentScreen instanceof HandledScreen<?>)) {
            this.playerInput = new PlayerInput(CInputs.forward, CInputs.backward, CInputs.left, CInputs.right, CInputs.jump, CInputs.sneak, CInputs.sprint);
            this.movementForward = this.getMovementMultiplier(CInputs.forward, CInputs.backward);
            this.movementSideways = this.getMovementMultiplier(CInputs.left, CInputs.right);
        }
    }
}
