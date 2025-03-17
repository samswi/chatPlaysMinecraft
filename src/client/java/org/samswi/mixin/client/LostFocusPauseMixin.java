package org.samswi.mixin.client;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Util;
import org.samswi.client.CInputs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class LostFocusPauseMixin {

    @Shadow private long lastWindowFocusedTime;

    @Inject(method = "render", at=@At("HEAD"))
    void modifyLastFocus(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci){
        if(CInputs.enabled)this.lastWindowFocusedTime = Util.getMeasuringTimeMs();
    }
}
