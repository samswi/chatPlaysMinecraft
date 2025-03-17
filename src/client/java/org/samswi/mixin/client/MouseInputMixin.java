package org.samswi.mixin.client;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.Nullable;
import org.samswi.client.CInputs;
import org.samswi.client.ChatControlsConfigScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)

public abstract class MouseInputMixin {
    @Shadow
    protected abstract boolean doAttack();

    @Shadow protected abstract void handleInputEvents();

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow protected abstract void handleBlockBreaking(boolean breaking);

    @Shadow protected abstract void doItemUse();

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickMouse(CallbackInfo ci) {
        if(CInputs.enabled && this.player != null && !CInputs.use) {this.handleInputEvents(); this.handleBlockBreaking(CInputs.breaking);}
        if(CInputs.attack)
            {if(this.player != null) this.doAttack();
            CInputs.attack = false;}
        if(CInputs.use && !(MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?>)  && !(MinecraftClient.getInstance().currentScreen instanceof ChatControlsConfigScreen) && !(MinecraftClient.getInstance().currentScreen instanceof GameMenuScreen) && this.player != null)
            {this.doItemUse();}
        if(CInputs.place && !(MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?>)  && !(MinecraftClient.getInstance().currentScreen instanceof ChatControlsConfigScreen) && !(MinecraftClient.getInstance().currentScreen instanceof GameMenuScreen))
            {if(this.player != null) this.doItemUse();
            CInputs.place = false;}
    }
}
