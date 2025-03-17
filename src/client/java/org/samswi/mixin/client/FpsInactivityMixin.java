package org.samswi.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.InactivityFpsLimiter;
import org.samswi.client.CInputs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InactivityFpsLimiter.class)
public class FpsInactivityMixin {
    @Shadow private int maxFps;
    @Shadow private MinecraftClient client;

    @Inject(method = "update", at=@At("HEAD"), cancellable = true)
    public void checkForChatControlEnabled(CallbackInfoReturnable<Integer> cir){
        if (!(this.client.getWindow().isMinimized()) && CInputs.enabled){
            cir.setReturnValue(this.maxFps);
        }
    }
}
