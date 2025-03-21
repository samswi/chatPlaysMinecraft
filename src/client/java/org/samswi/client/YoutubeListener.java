package org.samswi.client;

import com.github.kusaanko.youtubelivechat.AuthorType;
import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.IdType;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.text.SimpleDateFormat;
import java.util.List;

// this class is utilising kusaanko's YoutubeLiveChat library
// https://github.com/kusaanko/YouTubeLiveChat


public class YoutubeListener {

    private String liveChatid;
    public Boolean shouldRun = true;

    YoutubeListener(String liveChatid){
        this.liveChatid = liveChatid;
    }

    final MinecraftClient client = MinecraftClient.getInstance();

    public void startListening(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        YouTubeLiveChat chat = null;
        try {
            chat = new YouTubeLiveChat(this.liveChatid, false, IdType.VIDEO);
        } catch (Exception e) {
            client.getToastManager().add(new SystemToast(SystemToast.Type.NARRATOR_TOGGLE, Text.of("Youtube chat listener has failed to initialize!"), Text.of("Make sure you inputted the correct live id")));
            chatPlaysMCClient.currentChatType = null;
            chatPlaysMCClient.chatListenerEnabled = false;
            if(client.player != null) client.player.sendMessage(Text.literal("Failed to initialize youtube chat. Make sure you have correct live id").setStyle(Style.EMPTY.withColor(0xFF0000)), false);
            if(client.currentScreen instanceof ChatControlsConfigScreen) ((ChatControlsConfigScreen) client.currentScreen).init();
            throw new RuntimeException(e);
        }
        while (shouldRun) {
            try {
                chat.update();
            } catch (Exception e) {
                client.getToastManager().add(new SystemToast(SystemToast.Type.NARRATOR_TOGGLE, Text.of("Youtube chat listener has crashed!"), Text.of("Check logs for more info")));
                if(client.player != null) client.player.sendMessage(Text.literal("Youtube chat listener has crashed!").setStyle(Style.EMPTY.withColor(0xFF0000)), false);
                client.setScreen(new ChatControlsConfigScreen());
                chatPlaysMCClient.currentChatType = null;
                chatPlaysMCClient.chatListenerEnabled = false;
                throw new RuntimeException(e);
            }
            for (ChatItem item : chat.getChatItems()) {
                System.out.println(item.getAuthorType() + " || " + item.getMessage());
                CInputs.processChatCommands(item.getMessage());
                displayMessageInChat(item);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void stopListening(){
        this.shouldRun = false;
    }
    private int determineColor(List<AuthorType> types){
        if (types.contains(AuthorType.OWNER)) return 16766464;
        if (types.contains(AuthorType.MODERATOR)) return 6194417;
        if (types.contains(AuthorType.MEMBER)) return 2860590;
        if (types.contains(AuthorType.NORMAL)) return 16762823;
        return 16777215;

    }
    private void displayMessageInChat(ChatItem message){
        if (client.player != null) {

            Text chatText = Text.literal( message.getAuthorName()+ " ")
                    .setStyle(Style.EMPTY.withColor(determineColor(message.getAuthorType())).withBold(true))
                    .append(Text.literal(message.getMessage()).setStyle(Style.EMPTY.withColor(16777215).withBold(false)));

            client.player.sendMessage(chatText, false);
        }
    }
}
