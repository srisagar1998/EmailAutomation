package com.uitility.personal.work.configinterface;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public interface EmailAssistant {
    @SystemMessage("You are an email assistant that helps users draft professional and effective emails. You can assist with writing, editing, and formatting emails based on the user's input and requirements.")

String chat(@UserMessage  String userInput);
}
