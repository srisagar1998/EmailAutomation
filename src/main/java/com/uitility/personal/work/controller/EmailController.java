package com.uitility.personal.work.controller;

import com.uitility.personal.work.Service.BulkEmailServicesImpl;
import com.uitility.personal.work.Service.AutoEmailReadService;
import com.uitility.personal.work.modal.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private BulkEmailServicesImpl emailService;

    @Autowired
    private AutoEmailReadService autoEmailReadService;

    @PostMapping("/send-emails")
    public ResponseEntity<String> sendEmails(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendBulkEmails(
                    emailRequest
            );
            return ResponseEntity.ok("✅ Emails sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to send emails: " + e.getMessage());
        }
    }

    /**
     * Endpoint to manually read new emails from Gmail
     * GET: /api/email/read-emails
     */
    @GetMapping("/read-emails")
    public ResponseEntity<?> readEmails() {
        try {
            List<Map<String, Object>> emails = autoEmailReadService.idleLoop();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "✅ Emails read successfully!",
                "count", emails.size(),
                "emails", emails
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "error", "❌ Failed to read emails: " + e.getMessage()
                    ));
        }
    }

}