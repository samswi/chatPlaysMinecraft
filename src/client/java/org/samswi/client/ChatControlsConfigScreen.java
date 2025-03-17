package org.samswi.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ChatControlsConfigScreen extends Screen {
    TextFieldWidget twitchChannel;
    private String twitchChannelText;

    public ChatControlsConfigScreen() {
        super(Text.of("my config"));
    }

    @Override
    protected void init() {
        clearChildren();
        twitchChannel = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 40, 40 ,120, 20, Text.of("Select twitch channel"));
        ButtonWidget twitchConnect = ButtonWidget.builder(Text.of("Connect"), (btn) -> {
            // When the button is clicked, we can display a toast to the screen.
            runTwitch(twitchChannel.getText().toLowerCase());
            twitchChannelText = twitchChannel.getText().toLowerCase();
            this.init();
        }).dimensions(40, 65, 120, 20).build();

        twitchConnect.active = !twitchPlaysClient.chatListenerEnabled;

        this.addDrawableChild(twitchConnect);

        ButtonWidget closeListener = ButtonWidget.builder(Text.of("Close connection"), (btn) -> {
            // When the button is clicked, we can display a toast to the screen.
            twitchPlaysClient.chatListenerStop();
            this.init();
        }).dimensions(40, 90, 120, 20).build();

        closeListener.active = twitchPlaysClient.chatListenerEnabled;

        this.addDrawableChild(closeListener);

        this.addDrawableChild(twitchChannel);
        // Register the button widget.


    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
        context.drawText(this.textRenderer, "Input twitch username", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }

    public void runTwitch(String channel){
        twitchPlaysClient.chatListenerInitialize("twitch", channel);
    }
}

