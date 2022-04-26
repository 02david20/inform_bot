package com.example.basicwebscrape;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import com.example.basicwebscrape.news.NewsByTopic;
public class MyBot extends TelegramLongPollingBot {   
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.enableHtml(true);
            if(command.equals("/myname")){
                String msg = getBotUsername();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
                try {
                    execute(message);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }         
            else if (command.contains("/news")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn topic");
                
                // Inline keyboard buttons
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> InlineButtons = new ArrayList<>();
                List<InlineKeyboardButton> inlineKeyboarButtonList = new ArrayList<>();
                
                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
                
                inlineKeyboardButton1.setText("Thời sự");
                inlineKeyboardButton1.setCallbackData("thoi-su");
                inlineKeyboardButton2.setText("Thế giới");
                inlineKeyboardButton2.setCallbackData("the-gioi");
                inlineKeyboardButton3.setText("Tài chính-Kinh doanh");
                inlineKeyboardButton3.setCallbackData("tai-chinh-kinh-doanh");
                inlineKeyboardButton4.setText("Đời sống");
                inlineKeyboardButton4.setCallbackData("doi-song");
                inlineKeyboardButton5.setText("Văn hóa");
                inlineKeyboardButton5.setCallbackData("van-hoa");
                
                inlineKeyboarButtonList.add(inlineKeyboardButton1);
                inlineKeyboarButtonList.add(inlineKeyboardButton2);
                inlineKeyboarButtonList.add(inlineKeyboardButton3);
                inlineKeyboarButtonList.add(inlineKeyboardButton4);
                inlineKeyboarButtonList.add(inlineKeyboardButton5);
                
                InlineButtons.add(inlineKeyboarButtonList);
                inlineKeyboardMarkup.setKeyboard(InlineButtons);
                message.setReplyMarkup(inlineKeyboardMarkup);
                
                try {
                    execute(message);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }               
            else{
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Xin lỗi, câu lệnh của bạn không tồn tại");
                try {
                    execute(message);
                } 
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (update.hasCallbackQuery()) {
        	Message callbackMessage = update.getCallbackQuery().getMessage();
        	SendMessage message = new SendMessage();
        	CallbackQuery callbackQuery = update.getCallbackQuery();
        	String data = callbackQuery.getData();
        	NewsByTopic news = new NewsByTopic(data);
        	message.setChatId(callbackMessage.getChatId().toString());
            message.setText(news.getNewsByTopic());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        // TODO
        return "hungBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5336710924:AAFeLLC8O7ScaBwNWv-o_Jo_jAIJbFl3WdY";
    }
}
