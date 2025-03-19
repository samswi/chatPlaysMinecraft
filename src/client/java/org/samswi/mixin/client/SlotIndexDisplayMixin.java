package org.samswi.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.samswi.client.CInputs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class SlotIndexDisplayMixin {
    @Shadow @Nullable protected abstract Slot getSlotAt(double mouseX, double mouseY);

    @Inject(method = "drawSlot", at = @At("TAIL"))
    protected void drawIndex(DrawContext context, Slot slot, CallbackInfo ci){
        if(CInputs.enabled) context.drawText(MinecraftClient.getInstance().textRenderer, String.valueOf(slot.id), slot.x, slot.y, 0x80FFFFFF, true);
    }

}
