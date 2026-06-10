package com.uitility.personal.work.config;

import com.uitility.personal.work.configinterface.EmailAssistant;
//import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LongChainConfig {

    @Bean
    public EmailAssistant emailAssistant() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://127.0.0.1:1234/v1")
                .apiKey("dummy")
                .modelName("google/gemma-3-1b")
                .build();
     return AiServices.create(EmailAssistant.class, model);
    }

    @Bean
    public RestTemplate restTemplate() {
       //create a rest template bean
        return new RestTemplate();
    }

//    @Bean
//    CommandLineRunner testEmailAssistant(EmailAssistant emailAssistant) {
//        return args -> {
//            OpenAiChatModel model = OpenAiChatModel.builder()
//             .baseUrl("http://127.0.0.1:1234/v1")
//                    .apiKey("dummy")
//                    .modelName("google/gemma-3-1b")
//                    .logResponses(true)
//                    .logRequests(true)
//                    .build();
//            //String response = emailAssistant.chat("Write an email to schedule a meeting with the team next week.");
//           String response=model.chat("Write an email to schedule a meeting with the team next week.");
//            System.out.println("Generated Email: " + response);
//        };
//    }

}
