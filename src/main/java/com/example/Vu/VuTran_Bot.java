package com.example.Vu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class VuTran_Bot extends TelegramLongPollingBot {
    public String getOilPrice() {
        Document doc = null;
        String url = "https://www.pvoil.com.vn/truyen-thong/tin-gia-xang-dau";
        try {
            doc = Jsoup.connect(url).get();
            OilPrice oilprice = new OilPrice();
            String str = oilprice.returnOilPrice(doc, url);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "Có lỗi xảy ra";
        }
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
