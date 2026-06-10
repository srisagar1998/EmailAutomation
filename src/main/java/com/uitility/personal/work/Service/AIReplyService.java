package com.uitility.personal.work.Service;

import com.uitility.personal.work.configinterface.EmailAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIReplyService {


    private final EmailAssistant AIEmailAssistant;

    public AIReplyService(EmailAssistant AIEmailAssistant) {
        this.AIEmailAssistant = AIEmailAssistant;
    }
    public String generateReply(String emailContent) {
        // Use the AIEmailAssistant to generate a reply based on the email content
        return AIEmailAssistant.chat(emailContent);
    }
}
