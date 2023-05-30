package com.example.haikuBot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/generate", "start bot"),
            new BotCommand("/whatyoudo", "bot info")
    );
}