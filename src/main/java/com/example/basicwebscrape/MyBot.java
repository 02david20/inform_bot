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

public class MyBot extends TelegramLongPollingBot {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    public String getCurrentWeather(String cityName) {
        if (cityName != null && !cityName.isBlank()) {
            try {
                String urlString = "https://vi.wttr.in/" + URLEncoder.encode(cityName, StandardCharsets.UTF_8) + "?m?T?tqp0";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlString))
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                String result = body.substring(body.indexOf("<pre>") + 5, body.indexOf("</pre>"));
                result = result.replace("&quot;", "''");
                System.out.println(urlString);           
                //PrintStream out = new PrintStream(System.out, true, "UTF-8");
                //out.println(result);
                return result;
            }
            catch (Exception ex) {
                System.out.println("Có lỗi xảy ra");
                return "Có lỗi xảy ra";
            }
        }
        else {
            return "Vui lòng nhập tên thành phố !";
        }
    }
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());
            if(command.equals("/myname")){
                String msg = getBotUsername();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            if(command.contains("/weather")){
                String[] str = command.split(" ");
                String msg = getCurrentWeather(str[1]);
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
        // TODO
        return "InfoBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5124630324:AAGd47oSNGFSX2xhEdOmY8-G8BMD0v9rYDc";
    }
}
