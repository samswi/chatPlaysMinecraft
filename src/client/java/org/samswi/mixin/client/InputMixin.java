package org.samswi.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.samswi.client.ChatPlaysMCClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "tick", at = @At("TAIL"))
    public void modifyInput(CallbackInfo ci) {
        if (CInputs.enabled && ChatPlaysMCClient.chatListenerEnabled && !(client.currentScreen instanceof HandledScreen<?>)) {
            this.playerInput = new PlayerInput(CInputs.forward, CInputs.backward, CInputs.left, CInputs.right, CInputs.jump, CInputs.sneak, CInputs.sprint);
            float movementForward = getMovementMultiplier(CInputs.forward, CInputs.backward);
            float movementSideways = getMovementMultiplier(CInputs.left, CInputs.right);
            this.movementVector = (new Vec2f(movementSideways, movementForward)).normalize();
        }
        if (CInputs.sprint) client.options.sprintKey.setPressed(true);
    }
}
