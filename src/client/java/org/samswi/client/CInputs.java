package org.samswi.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CInputs {
    static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean forward;
    public static boolean backward;
    public static boolean left;
    public static boolean right;
    public static boolean sneak;
    public static boolean sprint;
    public static boolean jump;
    public static boolean attack;
    public static boolean breaking;
    public static boolean use;
    public static boolean place;

    public static boolean enabled;

    public static int wcd;
    public static int acd;
    public static int scd;
    public static int dcd;
    public static int jcd;
    public static int breakcd;
    public static int sneakcd;
    public static int usecd;
    public static int ecd;
    public static int sprintcd;
    public static int usedt;

    public static void processChatCommands(String chatMessage) {
        if (chatMessage.length() >= 2 && chatMessage.toLowerCase().startsWith("c ") && client.player != null && (client.currentScreen instanceof InventoryScreen || client.currentScreen instanceof CraftingScreen)) {
            String searchFor = chatMessage.substring(2);
            List<RecipeResultCollection> myList = Objects.requireNonNull(client.getNetworkHandler()).getSearchManager().getRecipeOutputReloadFuture().findAll(searchFor.toLowerCase(Locale.ROOT));
            assert client.interactionManager != null;
            client.interactionManager.clickRecipe(client.player.currentScreenHandler.syncId, myList.getFirst().getAllRecipes().getFirst().id(), false);
            if (client.currentScreen instanceof HandledScreen<?> handledScreen && client.interactionManager != null && !(client.currentScreen instanceof FurnaceScreen)) {
                client.interactionManager.clickSlot(handledScreen.getScreenHandler().syncId, 0, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        }
        switch (chatMessage.toLowerCase()) {
            case "a" -> {
                CInputs.left = true;
                CInputs.acd = 20;
            }
            case "d" -> {
                CInputs.right = true;
                CInputs.dcd = 20;
            }
            case "s" -> {
                CInputs.backward = true;
                CInputs.scd = 20;
            }
            case "jump" -> {
                CInputs.jump = true;
                CInputs.jcd = 10;
            }
            case "w" -> {
                CInputs.forward = true;
                CInputs.wcd = 20;
            }
            case "spr", "sprint" -> {
                CInputs.sprint = true;
                CInputs.sprintcd = 100;
            }

            case "snk", "sneak" -> {
                CInputs.sneak = true;
                CInputs.sneakcd = 100;
            }
            case "att", "attack" -> CInputs.attack = !CInputs.attack;
            case "place" -> {
                CInputs.place = true; CInputs.usedt = 10;
            }
            case "respawn" -> {if(client.player != null) {client.player.requestRespawn();}}
            case "break" -> {
                CInputs.breaking = true;
                CInputs.breakcd = 200;
            }
            case "use" -> {
                CInputs.use = true;
                CInputs.usecd = 40;
                CInputs.usedt = CInputs.usecd;
            }
            case "e" -> {
                if (CInputs.ecd == 0 && client.player != null && CInputs.enabled) {
                    if (client.currentScreen == null || client.currentScreen instanceof ChatScreen) {
                        client.execute(() -> {
                            client.setScreen(new InventoryScreen(client.player));
                            CInputs.ecd = 100;
                        });
                    } else if (!(client.currentScreen instanceof OptionsScreen) && !(client.currentScreen instanceof ChatControlsConfigScreen) && !(client.currentScreen instanceof GameMenuScreen)) {
                        client.execute(() -> client.setScreen(null));
                    }
                }
            }
        }

        if (chatMessage.toLowerCase().charAt(0) == 'r' && client.player != null) {
            if (chatMessage.toLowerCase().charAt(1) == 'y') {
                String[] splitted = chatMessage.split("y");
                float inputtedYaw = Float.parseFloat(splitted[1]);
                if (inputtedYaw >= -360 && inputtedYaw <= 360)
                    client.player.setYaw(client.player.getYaw() + inputtedYaw);
            } else if (chatMessage.toLowerCase().charAt(1) == 'p') {
                String[] splitted = chatMessage.split("p");
                float inputtedPitch = Float.parseFloat(splitted[1]);
                if (inputtedPitch >= -360 && inputtedPitch <= 360)
                    client.player.setPitch(client.player.getPitch() + inputtedPitch);
            }
        } else if (chatMessage.toLowerCase().startsWith("i ")) {
            String[] splited = chatMessage.split(" ");
            twitchPlaysClient.swapSlots(Integer.parseInt(splited[1]), Integer.parseInt(splited[2]));
        } else if (chatMessage.contains(" ")) {
            String[] splited = chatMessage.split(" ");
            try {
                int ticks = Integer.parseInt(splited[1]);
                if (ticks < 0) return;
                switch (splited[0].toLowerCase()) {
                    case "a" -> {
                        CInputs.left = true;
                        CInputs.acd = ticks;
                    }
                    case "d" -> {
                        CInputs.right = true;
                        CInputs.dcd = ticks;
                    }
                    case "s" -> {
                        CInputs.backward = true;
                        CInputs.scd = ticks;
                    }
                    case "jump" -> {
                        CInputs.jump = true;
                        CInputs.jcd = ticks;
                    }
                    case "w" -> {
                        CInputs.forward = true;
                        CInputs.wcd = ticks;
                    }
                    case "break" -> {
                        CInputs.breaking = true;
                        CInputs.breakcd = ticks;
                    }
                    case "snk" -> {
                        CInputs.sneak = true;
                        CInputs.sneakcd = ticks;
                    }
                    case "h" -> {
                        if (ticks >= 1 && ticks <= 9 && client.player != null) {
                            client.player.getInventory().selectedSlot = (ticks - 1);
                        }
                    }
                }
            } catch (NumberFormatException ignored) {}
        }
    }
}

