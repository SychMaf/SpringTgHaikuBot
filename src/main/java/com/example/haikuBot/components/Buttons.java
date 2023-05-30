package com.example.haikuBot.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Generate");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");

    public static InlineKeyboardMarkup startInlineMarkup() {
        START_BUTTON.setCallbackData("/generate");

        List<InlineKeyboardButton> anotherLine = List.of(START_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(anotherLine);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/generate");
        HELP_BUTTON.setCallbackData("/whatyoudo");

        List<InlineKeyboardButton> rowInline = List.of(HELP_BUTTON);
        List<InlineKeyboardButton> anotherLine = List.of(START_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline, anotherLine);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

}
