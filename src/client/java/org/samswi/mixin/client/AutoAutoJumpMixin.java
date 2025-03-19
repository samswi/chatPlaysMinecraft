package org.samswi.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import org.samswi.client.CInputs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class AutoAutoJumpMixin {
    @Inject(method = "shouldAutoJump", at=@At("HEAD"), cancellable = true)
    protected void chatControlEnabledOverride(CallbackInfoReturnable<Boolean> cir){
        if (CInputs.enabled) cir.setReturnValue(true);
    }
}
