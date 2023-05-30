package com.example.haikuBot;

import com.example.haikuBot.config.BotConfig;
import com.example.haikuBot.handler.DefaultCommand;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URISyntaxException;

import static com.example.haikuBot.components.BotCommands.LIST_OF_COMMANDS;

@Slf4j
@Component
public class HaikuBotTg extends TelegramLongPollingBot {
    private final DefaultCommand defaultCommand;
    final BotConfig config;

    public HaikuBotTg(BotConfig config, DefaultCommand defaultCommand) {
        this.defaultCommand = defaultCommand;
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        log.info("smf start");
        long chatId;
        String receivedMessage;

        if (update.hasMessage()) {
            config.setChatId(update.getMessage().getChatId().toString());
            chatId = update.getMessage().getChatId();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                try {
                    botAnswerUtils(receivedMessage, chatId);
                } catch (TelegramApiException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (update.hasCallbackQuery()) {
            config.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            chatId = update.getCallbackQuery().getMessage().getChatId();
            receivedMessage = update.getCallbackQuery().getData();

            try {
                botAnswerUtils(receivedMessage, chatId);
            } catch (TelegramApiException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId) throws TelegramApiException, URISyntaxException {
        switch (receivedMessage) {
            case "/generate" -> execute(defaultCommand.startBotOperation(chatId));
            case "/whatyoudo" -> execute(defaultCommand.helpBotOperation(chatId));
            default -> {
            }
        }
    }
}
