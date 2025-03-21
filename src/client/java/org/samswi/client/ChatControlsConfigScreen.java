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
    TextFieldWidget youtubeLink;
    private String twitchChannelText;

    public ChatControlsConfigScreen() {
        super(Text.of("my config"));
    }

    @Override
    protected void init() {
        clearChildren();
        twitchChannel = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 40, 40 ,120, 20, Text.of("Select twitch channel"));
        ButtonWidget twitchConnect = ButtonWidget.builder(Text.of("Connect"), (btn) -> {
            runTwitch(twitchChannel.getText().toLowerCase());
            twitchChannelText = twitchChannel.getText().toLowerCase();
            this.init();
        }).dimensions(40, 65, 120, 20).build();

        twitchConnect.active = !chatPlaysMCClient.chatListenerEnabled;

        this.addDrawableChild(twitchConnect);

        ButtonWidget closeListener = ButtonWidget.builder(Text.of("Close connection"), (btn) -> {
            chatPlaysMCClient.chatListenerStop();
            this.init();
        }).dimensions(40, 90, 245, 20).build();

        closeListener.active = chatPlaysMCClient.chatListenerEnabled;

        this.addDrawableChild(closeListener);
        this.addDrawableChild(twitchChannel);



        youtubeLink = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 165, 40 ,120, 20, Text.of("Input youtube live id"));
        ButtonWidget youtubeConnect = ButtonWidget.builder(Text.of("Connect"), (btn) -> {
            runYoutube(youtubeLink.getText());
            this.init();
        }).dimensions(165, 65, 120, 20).build();

        youtubeConnect.active = !chatPlaysMCClient.chatListenerEnabled;

        this.addDrawableChild(youtubeConnect);
        this.addDrawableChild(youtubeLink);




    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
        context.drawText(this.textRenderer, "Input twitch username", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
        context.drawText(this.textRenderer, "Input youtube live id", 165, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }

    public void runTwitch(String channel){
        chatPlaysMCClient.chatListenerInitialize("twitch", channel);
    }
    public void runYoutube(String link){
        chatPlaysMCClient.chatListenerInitialize("youtube", link);
    }
}

