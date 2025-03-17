package org.samswi.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class twitchPlaysClient implements ClientModInitializer {

    private static KeyBinding toggleKey;
    private static KeyBinding configKey;
    public static boolean chatListenerEnabled = false;
    public static String currentChatType = null;
    public static TwitchListener twitchInstance = new TwitchListener("mud_flaps123");
    public static Thread twitchThread = null;
    public static String twitchChannel = null;
    //    public static String currentYoutube;
    public static int selectColor(){
        if (currentChatType.equals("twitch")) {return 0xFFa970ff;}
        if (currentChatType.equals("youtube")) {return 0xFFff0931;}
        return 0xFF000000;
    }

    @Override
    public void onInitializeClient() {

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.examplemod.spook", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.examplemod.test" // The translation key of the keybinding's category.
        ));
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.examplemod.config", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_M, // The keycode of the key
                "category.examplemod.test" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                if(CInputs.enabled) {
                    client.player.sendMessage(Text.literal("Chat control disabled!"), false);
                    CInputs.enabled = false;
                } else if (!CInputs.enabled) {
                    client.player.sendMessage(Text.literal("Chat control enabled!"), false);
                    CInputs.enabled = true;
                }
            }

            while (configKey.wasPressed()) {
                if (!(client.currentScreen instanceof ChatControlsConfigScreen)) {
                    client.setScreen(new ChatControlsConfigScreen());
                }
            }
            if (CInputs.acd == 0) CInputs.left = false;
            if (CInputs.scd == 0) CInputs.backward = false;
            if (CInputs.dcd == 0) CInputs.right = false;
            if (CInputs.wcd == 0) CInputs.forward = false;
            if (CInputs.breakcd == 0) CInputs.breaking = false;
            if (CInputs.jcd == 0) CInputs.jump = false;
            if (CInputs.sneakcd == 0) CInputs.sneak = false;
            if (CInputs.usecd == 0) CInputs.use = false;

            if (CInputs.acd > 0) CInputs.acd--;
            if (CInputs.wcd > 0) CInputs.wcd--;
            if (CInputs.scd > 0) CInputs.scd--;
            if (CInputs.dcd > 0) CInputs.dcd--;
            if (CInputs.jcd > 0) CInputs.jcd--;
            if (CInputs.breakcd > 0) CInputs.breakcd--;
            if (CInputs.sneakcd > 0) CInputs.sneakcd--;
            if (CInputs.usecd > 0) CInputs.usecd--;
            if (CInputs.ecd > 0) CInputs.ecd--;

        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
                    final MinecraftClient client = MinecraftClient.getInstance();
                    context.getMatrices().push();
                    context.getMatrices().scale(1.5f, 1.5f, 1.5f);
                if (!chatListenerEnabled) {
                    context.drawText(client.textRenderer, "Chat not connected", 5, 5, 0x80808080, true);
                } else {
                    if (CInputs.enabled){
                        context.drawText(client.textRenderer, "W", 5, 5, CInputs.forward ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "A", 15, 5, CInputs.left ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "S", 25, 5, CInputs.backward ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "D", 35, 5, CInputs.right ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "Jump", 45, 5, CInputs.jump ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "Sneak", 5, 15, CInputs.sneak ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "Sprint", 39, 15, CInputs.sprint ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "Break", 5, 25, CInputs.breaking ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, CInputs.ecd > 0 ? String.valueOf((int)(Math.ceil(CInputs.ecd / 20.0))) : "E", 39, 25, (CInputs.ecd > 0) ? selectColor() : 0xFF808080, true);
                        context.drawText(client.textRenderer, "Use", 51, 25, CInputs.use ? selectColor() : 0xFF808080, true);
                    }else{
                        context.drawText(client.textRenderer, "Chat control disabled", 5, 5, 0xFF0080FF, true);
                        if (Objects.equals(currentChatType, "twitch")) {
                            context.drawText(client.textRenderer, ("twitch.tv/" + twitchChannel), 5, 15, 0xB0a970ff, true);
                        }

                    }
                }
            context.getMatrices().pop();
        });


    }

//    public static void swapSlots(HandledScreen<?> screen, int slotIndex1, int slotIndex2) {
//        // Access the ScreenHandler (server-side logic) linked to this HandledScreen
//        ScreenHandler handler = screen.getScreenHandler();
//
//        // Validate slot indices
//        if (slotIndex1 < 0 || slotIndex2 < 0 ||
//                slotIndex1 >= handler.slots.size() ||
//                slotIndex2 >= handler.slots.size()) {
//            throw new IllegalArgumentException("Invalid slot index for swapping.");
//        }
//
//        // Get the slots
//        Slot slot1 = handler.getSlot(slotIndex1);
//        Slot slot2 = handler.getSlot(slotIndex2);
//
//        // Swap ItemStacks between the two slots
//        ItemStack stack1 = slot1.getStack();
//        ItemStack stack2 = slot2.getStack();
//        slot1.setStack(stack2);
//        slot2.setStack(stack1);
//    }

    public static void swapSlots(int index1, int index2){
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.currentScreen instanceof HandledScreen<?> handledScreen && client.interactionManager != null){
            client.interactionManager.clickSlot(handledScreen.getScreenHandler().syncId, index1, 0, SlotActionType.PICKUP, client.player);
            client.interactionManager.clickSlot(handledScreen.getScreenHandler().syncId, index2, 0, SlotActionType.PICKUP, client.player);
            client.interactionManager.clickSlot(handledScreen.getScreenHandler().syncId, index1, 0, SlotActionType.PICKUP, client.player);
        }
    }

    public static void chatListenerInitialize(String type, String channel) {
        final MinecraftClient client = MinecraftClient.getInstance();
        if (twitchPlaysClient.chatListenerEnabled) {client.getToastManager().add(SystemToast.create(client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Chat listener already running"), Text.of(""))); ; return;}
        if (type.equals("twitch")) {
            if (twitchThread == null || !twitchThread.isAlive()) {
                twitchInstance = new TwitchListener(channel);
                twitchThread = new Thread(twitchInstance::startListening);
                twitchThread.start();
                twitchPlaysClient.chatListenerEnabled = true;
                twitchPlaysClient.currentChatType = "twitch";
                twitchChannel = channel;
                MinecraftClient.getInstance().getToastManager().add(SystemToast.create(MinecraftClient.getInstance(), SystemToast.Type.NARRATOR_TOGGLE, Text.of("Twitch chat connected!"), Text.of(("Listening to: " + channel))));
            }
        }
    }

    public static void chatListenerStop() {
        if (!twitchPlaysClient.chatListenerEnabled) return;
        if (currentChatType.equals("twitch")) {
            if (twitchInstance != null) {
                twitchInstance.stopListening(); // signals shouldRun = false
                try {
                    if (twitchThread != null) {
                        twitchThread.join(); // waits safely for thread termination
                        twitchThread = null;
                        twitchPlaysClient.chatListenerEnabled = false;
                        twitchPlaysClient.currentChatType = null;
                        twitchChannel = null;
                        MinecraftClient.getInstance().getToastManager().add(SystemToast.create(MinecraftClient.getInstance(), SystemToast.Type.NARRATOR_TOGGLE, Text.of("Disconnected from twitch chat!"), Text.of("")));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            else {
                MinecraftClient.getInstance().getToastManager().add(SystemToast.create(MinecraftClient.getInstance(), SystemToast.Type.NARRATOR_TOGGLE, Text.of("No listener running!"), Text.of("")));
            }

        }
    }


}

