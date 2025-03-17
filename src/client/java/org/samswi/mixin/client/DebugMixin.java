package org.samswi.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class DebugMixin {
    @Inject(method = "onSlotClick", at = @At("HEAD"))
    void printSlot(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci){
        System.out.println("slotIndex: " + slotIndex + ", button: " + button + ", actionType: " + actionType);
    }
}
