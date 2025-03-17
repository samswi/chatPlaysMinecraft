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
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;
import java.util.Locale;

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

    public static void processChatCommands(String chatMessage) {
        MinecraftClient client = MinecraftClient.getInstance();
        System.out.println("Detected message");
        if (chatMessage.length() >= 2 && chatMessage.toLowerCase().startsWith("c ") && client.player != null && (client.currentScreen instanceof InventoryScreen || client.currentScreen instanceof CraftingScreen)) {
            String searchFor = chatMessage.substring(2);
            List<RecipeResultCollection> myList = MinecraftClient.getInstance().getNetworkHandler().getSearchManager().getRecipeOutputReloadFuture().findAll(searchFor.toLowerCase(Locale.ROOT));
            client.interactionManager.clickRecipe(client.player.currentScreenHandler.syncId, myList.getFirst().getAllRecipes().getFirst().id(), false);
            if(client.currentScreen instanceof HandledScreen<?> handledScreen && client.interactionManager != null && !(client.currentScreen instanceof FurnaceScreen)) {
                client.interactionManager.clickSlot(handledScreen.getScreenHandler().syncId, 0, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        }
        switch (chatMessage.toLowerCase()) {
            case "a" -> { CInputs.left = true; CInputs.acd = 20; }
            case "d" -> { CInputs.right = true; CInputs.dcd = 20; }
            case "s" -> { CInputs.backward = true; CInputs.scd = 20; }
            case "jump" -> { CInputs.jump = true; CInputs.jcd = 10; }
            case "w" -> { CInputs.forward = true; CInputs.wcd = 20; }
            case "spr" -> {CInputs.sprint = !CInputs.sprint;}
            case "sprint" -> {CInputs.sprint = !CInputs.sprint;}
            case "snk" -> {CInputs.sneak = true; CInputs.sneakcd = 100;}
            case "sneak" -> {CInputs.sneak = true; CInputs.sneakcd = 100;}
            case "att" -> CInputs.attack = !CInputs.attack ;
            case "attack" -> CInputs.attack = !CInputs.attack ;
            case "place" -> CInputs.place = true;
            case "respawn" -> MinecraftClient.getInstance().player.requestRespawn();
            case "break" -> {CInputs.breaking = true; CInputs.breakcd = 200;}
            case "use" -> {CInputs.use = true; CInputs.usecd = 40;}
            case "e" -> {
                if (CInputs.ecd == 0 && client.player != null && CInputs.enabled) {
                    if (client.currentScreen == null || client.currentScreen instanceof ChatScreen) {
                        MinecraftClient.getInstance().execute(() -> {
                            MinecraftClient.getInstance().setScreen(new InventoryScreen(MinecraftClient.getInstance().player));
                            CInputs.ecd = 100;
                        });
                    }else if (!(client.currentScreen instanceof OptionsScreen) && !(client.currentScreen instanceof ChatControlsConfigScreen) && !(client.currentScreen instanceof GameMenuScreen)){
                        MinecraftClient.getInstance().execute(() -> {
                            MinecraftClient.getInstance().setScreen(null);
                        });
                    }
                }
            }
        }

        if (chatMessage.toLowerCase().charAt(0) == 'r') {
            try{
                if (chatMessage.toLowerCase().charAt(1) == 'y') {
                    String[] splited = chatMessage.split("y");
                        Float inputedYaw = Float.parseFloat(splited[1]);
                        if (inputedYaw >= -360 && inputedYaw <= 360)
                            client.player.setYaw(client.player.getYaw() + inputedYaw);
                } else if (chatMessage.toLowerCase().charAt(1) == 'p') {
                    String[] splited = chatMessage.split("p");
                        Float inputedPitch = Float.parseFloat(splited[1]);
                        if (inputedPitch >= -360 && inputedPitch <= 360)
                            client.player.setPitch(client.player.getPitch() + inputedPitch);

                }
            } catch (Exception e) {
                return;
            }
        } else if (chatMessage.contains(" ")) {
            try {
                String[] splited = chatMessage.split(" ");
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
                        if (ticks >=1 && ticks <= 9 && client.player != null) {
                            client.player.getInventory().selectedSlot = (ticks - 1);
                        }
                    }
                }
            }
            catch (Exception e) {return;}
        }

        if (chatMessage.toLowerCase().startsWith("i ")) {
            System.out.println("detected i");
            String[] splited = chatMessage.split(" ");
            try
            {
                twvsytClient.swapSlots(Integer.parseInt(splited[1]), Integer.parseInt(splited[2]));
                System.out.println("Attemp succeesful");
            }
            catch (Exception e){
                System.out.println("Something went wrong");
                return;
            }
        }
        else if(chatMessage.length() >=3 ) {
            if (chatMessage.substring(0, 3).toLowerCase().contains("spr")) CInputs.sprint = !CInputs.sprint;
            else if (chatMessage.substring(0, 3).toLowerCase().contains("att")) CInputs.attack = true;
            else if (chatMessage.substring(0, 3).toLowerCase().contains("snk")) CInputs.sneak = !CInputs.sneak;
            else if(chatMessage.length() >= 5) {if (chatMessage.substring(0, 5).toLowerCase().contains("break")) CInputs.sprint = !CInputs.sprint;}
        }
//        else if (chatMessage.toLowerCase().contains("e") && CInputs.ecd == 0) {
//            MinecraftClient.getInstance().execute(() -> {
//                MinecraftClient.getInstance().setScreen(new InventoryScreen(MinecraftClient.getInstance().player));
//                CInputs.ecd = 100;
//            }
//            );
//        };
    }
}

