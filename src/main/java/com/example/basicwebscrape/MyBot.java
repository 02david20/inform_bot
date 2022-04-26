package com.example.basicwebscrape;

import java.io.PrintStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import com.example.basicwebscrape.football.*;

public class MyBot extends TelegramLongPollingBot {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Câu lệnh không hợp lệ");
            if (command.equals("/infobot")) {
            	String msg = "Đây là con bot nhỏ của một thằng ngông cuồng.";
            	message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            else if (command.equals("/start")){
                String msg = "Gõ / để hiện danh sách các lệnh.";
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            else if (command.equals("/matches")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn giải đấu");
            	Buttons buttons = new Buttons();
				message.setReplyMarkup(buttons.setButtons("matches"));
            }
            else if (command.equals("/standing")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn giải đấu");
            	Buttons buttons = new Buttons();
				message.setReplyMarkup(buttons.setButtons("standing"));
            }
            else if (command.equals("/scorers")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn giải đấu");
            	Buttons buttons = new Buttons();
				message.setReplyMarkup(buttons.setButtons("scorers"));
            }
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if (update.hasCallbackQuery()) {
        	Message msg = update.getCallbackQuery().getMessage();
        	CallbackQuery callbackQuery = update.getCallbackQuery();
        	String data = callbackQuery.getData();
        	SendMessage message = new SendMessage();
        	message.setChatId(msg.getChatId().toString());
        	Standing standing = new Standing();
        	Matches matches = new Matches();
        	Scorers scorers = new Scorers();
        	String league = data.split("_")[0];
        	String type = data.split("_")[1];
        	
        	if (type.equals("standing")) {
        		message.setText(standing.getMessage(league));
        	}
        	if (type.equals("matches")) {
        		message.setText(matches.getMessage(league));
        	}
        	if (type.equals("scorers")) {
        		message.setText(scorers.getMessage(league));
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
        // TODO
        return "HuuDanhBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5241098510:AAFv91GSHPZSlMGgvdD3rU3d9rzpwHBadyU";
    }
}
