package org.samswi.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.minecraft.text.Style;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

// shoutout chatGPT

public class TwitchListener {

    String TWITCH_IRC_SERVER = "irc.chat.twitch.tv";
    int TWITCH_IRC_PORT = 6667;
    String NICKNAME = "justinfan12345";
    public String CHANNEL;

    Socket socket;
    BufferedReader reader;
    OutputStreamWriter writer;

    public TwitchListener(String selectedChannel) {
        this.CHANNEL = selectedChannel;
    }
    public void setChannel(String channel){
        this.CHANNEL = channel;
    }

    public void startListening() {
        final MinecraftClient client = MinecraftClient.getInstance();

        try{
             socket = new Socket(TWITCH_IRC_SERVER, TWITCH_IRC_PORT);
             reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             writer = new OutputStreamWriter(socket.getOutputStream());

            performLogin(writer);
            System.out.println("Connected to Twitch chat! Listening to #" + CHANNEL);

            processChatMessages(reader, writer, client);

        } catch (Exception e) {
            System.err.println("Error connecting to Twitch chat: " + e.getMessage());
            CInputs.enabled = false;
            e.printStackTrace();
        }
    }
    
    public void stopListening() {
        try {
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error closing Twitch chat connection: " + e.getMessage());
            MinecraftClient.getInstance().getToastManager().add(SystemToast.create(MinecraftClient.getInstance(), SystemToast.Type.NARRATOR_TOGGLE, Text.of("There was a problem with closing connection"), Text.of("Check logs for more info.")));
            throw new RuntimeException(e);
        }
    }

    private void performLogin(OutputStreamWriter writer) throws Exception {
        writer.write("PASS oauth:anonymous\n");
        writer.write("NICK " + NICKNAME + "\n");
        writer.write("JOIN #" + CHANNEL + "\n");
        writer.write("CAP REQ :twitch.tv/tags\n");
        writer.flush();
    }

    private void processChatMessages(BufferedReader reader, OutputStreamWriter writer, MinecraftClient client) throws Exception {
        try {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PING")) {
                    writer.write("PONG :tmi.twitch.tv\n");
                    writer.flush();
                    continue;
                }
                if (line.contains("PRIVMSG")) {
                    handleChatMessage(line, client);
                }
            }
        } catch (SocketException e) {
            if ("Socket closed".equals(e.getMessage())) {
                System.out.println("Twitch Listener stopped gracefully (socket closed).");
            } else {
                System.err.println("Unexpected socket error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("IOException in processChatMessages: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void handleChatMessage(String line, MinecraftClient client) {
        try {
            String[] tags = line.split(";");
            String userColor = extractTagValue(tags, "color=", "#FFFFFF");
            String rawUsername = extractTagValue(tags, "display-name=", "Anonymous");

            String chatMessage = extractChatMessage(line);
            if (chatMessage == null) return;

            CInputs.processChatCommands(chatMessage);

            displayMessageInMinecraft(client, rawUsername, userColor, chatMessage);

        } catch (Exception e) {
            System.err.println("Error handling chat message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String extractTagValue(String[] tags, String prefix, String defaultValue) {
        for (String tag : tags) {
            if (tag.startsWith(prefix)) {
                String[] keyValue = tag.split("=", 2);
                if (keyValue.length > 1 && !keyValue[1].isEmpty()) {
                    return keyValue[1];
                }
            }
        }
        return defaultValue;
    }

    private String extractChatMessage(String line) {
        String splitKey = "PRIVMSG #" + CHANNEL + " :";
        if (line.contains(splitKey)) {
            return line.split(splitKey, 2)[1];
        }
        return null;
    }

    private void displayMessageInMinecraft(MinecraftClient client, String username, String userColor, String message) {
        if (client.player != null) {
            int colorInt = Integer.decode(userColor);

            Text chatText = Text.literal(username + " ")
                    .setStyle(Style.EMPTY.withColor(colorInt).withBold(true))
                    .append(Text.literal(message).setStyle(Style.EMPTY.withColor(16777215).withBold(false)));

            client.player.sendMessage(chatText, false);
        }
    }
}