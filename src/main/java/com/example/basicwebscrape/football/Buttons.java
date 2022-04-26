package com.example.basicwebscrape.football;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Buttons {
	public InlineKeyboardMarkup setButtons(String type) {
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List < List < InlineKeyboardButton >> buttons = new ArrayList<>();
		List < InlineKeyboardButton > button_list1 = new ArrayList<>();
		List < InlineKeyboardButton > button_list2 = new ArrayList<>();
		List < InlineKeyboardButton > button_list3 = new ArrayList<>();

		InlineKeyboardButton england = new InlineKeyboardButton();
		england.setText("Premier League");
		england.setCallbackData("England_" + type);
		InlineKeyboardButton spain = new InlineKeyboardButton();
		spain.setText("Laliga Santander");
		spain.setCallbackData("Spain_" + type);
		InlineKeyboardButton italy = new InlineKeyboardButton();
		italy.setText("Serie A");
		italy.setCallbackData("Italy_" + type);
		InlineKeyboardButton germany = new InlineKeyboardButton();
		germany.setText("Bundesliga");
		germany.setCallbackData("Germany_" + type);
		InlineKeyboardButton france = new InlineKeyboardButton();
		france.setText("Ligue 1");
		france.setCallbackData("France_" + type);
		InlineKeyboardButton vietnam = new InlineKeyboardButton();
		vietnam.setText("V-League");
		vietnam.setCallbackData("Vietnam_matches");
		
		button_list1.add(england);
		button_list1.add(spain);
		button_list2.add(italy);
		button_list2.add(germany);
		button_list3.add(france);
		button_list3.add(vietnam);
		
		buttons.add(button_list1);
		buttons.add(button_list2);
		buttons.add(button_list3);
		markupInline.setKeyboard(buttons);
		
		return markupInline;
	}
}
