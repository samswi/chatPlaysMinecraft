package org.samswi.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ChatPlaysCommand {
    private static final String[] SUGGESTED_OPTIONS = {"allowCustomTimings", "wtime", "stime", "atime", "dtime", "sprinttime","sneaktime", "breaktime", "usetime"};

    private static final SuggestionProvider<FabricClientCommandSource> OPTION_SUGGESTIONS =
            (context, builder) -> {
                for (String suggestion : SUGGESTED_OPTIONS) {
                    builder.suggest(suggestion);
                }
                return CompletableFuture.completedFuture(builder.build());
            };


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        dispatcher.register(literal("chatplays")
                .then(literal("set")
                .then(argument("option name", StringArgumentType.string())
                        .suggests(OPTION_SUGGESTIONS)
                .then(argument("value", IntegerArgumentType.integer(0))
                        .executes(context ->{
                            ModConfig.updateValue(StringArgumentType.getString(context, "option name"), IntegerArgumentType.getInteger(context, "value"));
                            MinecraftClient.getInstance().player.sendMessage(Text.literal("Set the " + StringArgumentType.getString(context, "option name") + "value to " + IntegerArgumentType.getInteger(context, "value")), false);
                            return IntegerArgumentType.getInteger(context, "value");
                        })))));

    }
}
