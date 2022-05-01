package com.example.basicwebscrape;

import com.example.basicwebscrape.OilPrice.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {
    public String getOilPrice() {
        OilPrice oilPrice = OilPrice.getOilPriceInstance();
        return oilPrice.getOilPrice();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());
            if(command.equals("/oilprice")){
                String msg = getOilPrice();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "VuTran_bot";
    }

    @Override
    public String getBotToken() {
        return "5374520206:AAEoh4UFpS15nbVVYb5FwIhhiX37a_gHBaw";
    }
}
