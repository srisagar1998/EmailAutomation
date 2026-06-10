package com.uitility.personal.work.Service;


import com.uitility.personal.work.Exception.MyException;
import com.uitility.personal.work.entity.EmailEntity;
import com.uitility.personal.work.modal.EmailRequest;
import com.uitility.personal.work.repo.EmailRepo;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.Map;

@Service
public class BulkEmailServicesImpl {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepo emailRepo;

    public void sendBulkEmails(EmailRequest emailRequest) {
        for (Map.Entry<String, String> entry : emailRequest.getEmails().entrySet()) {
            String hrName = entry.getKey();
            String emailId = entry.getValue();

            EmailEntity log = new EmailEntity();
            log.setHRName(hrName);
            log.setEmailId(emailId);
            log.setCompanyName(extractCompanyName(emailId));
            log.setMobileNo(emailRequest.getMobileNo());
            log.setDate(new Date());
            log.setStatus(emailRequest.getStatus());

            try {
                // ✅ Personalized HTML
                String htmlContent = getHtmlContent(hrName);

                // ✅ Send Email
                sendEmail(emailId, emailRequest.getSubject(), htmlContent, emailRequest.getAttachments());


            } catch (Exception e) {
                     throw  new MyException("Failed to send email to " + emailId + ": " + e.getMessage());
            }

            // ✅ Save log in DB
            emailRepo.save(log);
        }
    }

    private void sendEmail(String toEmail, String subject, String htmlContent, java.util.List<String> attachmentPaths) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        // ✅ Multiple attachments
        if (attachmentPaths != null) {
            for (String path : attachmentPaths) {
                FileSystemResource file = new FileSystemResource(new File(path));
                if (file.exists()) {
                    helper.addAttachment(file.getFilename(), file);
                }
            }
        }else{
            throw new MyException("Attachment paths list is null");
        }

        mailSender.send(message);
    }

    // ✅ Dynamic HTML Template
    private String getHtmlContent(String hrName) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\" />\n" +
                "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\" />\n" +
                "<title>Java Backend Developer Application</title>\n" +
                "<style>\n" +
                "  body { margin:0; padding:0; background:#eef2f7; font-family:Arial,Helvetica,sans-serif; }\n" +
                "  .wrapper { width:100%; padding:24px 0; }\n" +
                "  .card { max-width:760px; margin:auto; background:#fff; border-radius:14px; overflow:hidden; box-shadow:0 8px 25px rgba(0,0,0,0.1); }\n" +
                "  .header h1 { margin:0; font-size:26px; font-weight:700; }\n" +
                "  .header p { margin:6px 0 0; font-size:15px; color:#c7d2fe; }\n" +
                "\n" +
                "  .section { padding:28px; }\n" +
                "  .section h2 { font-size:19px; margin:0 0 14px; color:#111827; border-left:4px solid #2563eb; padding-left:10px; }\n" +
                "  .p { font-size:15px; line-height:1.6; color:#374151; margin:0 0 16px; }\n" +
                "\n" +
                "  .badge { display:inline-block; font-size:13px; font-weight:500; color:#1f2937;\n" +
                "           background:#e0f2fe; border:1px solid #38bdf8;\n" +
                "           padding:7px 12px; border-radius:999px; margin:0 8px 8px 0; }\n" +
                "\n" +
                "  .timeline { border-left:3px solid #2563eb; padding-left:16px; margin:0; list-style:none; }\n" +
                "  .timeline li { margin-bottom:12px; font-size:14px; color:#374151; }\n" +
                "  .timeline strong { color:#111827; }\n" +
                "\n" +
                "  .footer { background:#f9fafb; padding:20px; font-size:13px; color:#6b7280; text-align:center; }\n" +
                "  .footer a { margin:0 6px; text-decoration:none; color:#2563eb; }\n" +
                "  .footer svg { vertical-align:middle; }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"wrapper\">\n" +
                "    <table role=\"presentation\" class=\"card\">\n" +
                "      <tr>\n" +
                "        <td>\n" +
                "         <!-- INTRO -->\n" +
                "          <div class=\"section\">\n" +
                "            <p class=\"p\">Dear <strong>"+hrName+"</strong>,</p>\n" +
                "            <p class=\"p\">I hope you’re doing well.</p>\n" +
                "            <p class=\"p\">\n" +
                "              I am writing to express my keen interest in the <strong>Java Backend Developer</strong> position.I came across your job posting for a <strong>Java Developer </strong>position on LinkedIn, and it immediately caught my attention. \n" +
                "              With over <strong>5 years of expertise</strong> in designing scalable backend systems using \n" +
                "              <em>Java, Spring Boot, and Microservices</em>, I have successfully delivered enterprise-level projects, optimized performance, and improved reliability.\n" +
                "            </p>\n" +
                "            <p class=\"p\">\n" +
                "              I am confident that my hands-on experience in <strong>REST APIs, CI/CD pipelines, cloud (AWS/GCP)</strong>, and \n" +
                "              <strong>database management</strong> will allow me to contribute effectively from day one.\n" +
                "            </p>\n" +
                "          </div>\n" +
                "\n" +
                "          <!-- SKILLS -->\n" +
                "          <div class=\"section\" style=\"background:#f9fafb;\">\n" +
                "            <h2>Core Skills</h2>\n" +
                "            <p>\n" +
                "              <span class=\"badge\">Java</span>\n" +
                "              <span class=\"badge\">Spring Boot</span>\n" +
                "              <span class=\"badge\">Microservices</span>\n" +
                "              <span class=\"badge\">REST APIs</span>\n" +
                "              <span class=\"badge\">AWS / GCP</span>\n" +
                "              <span class=\"badge\">CI/CD</span>\n" +
                "              <span class=\"badge\">Multithreading</span>\n" +
                "              <span class=\"badge\">Design Patterns</span>\n" +
                "              <span class=\"badge\">SQL / Hibernate</span>\n" +
                "              <span class=\"badge\">Agile</span>\n" +
                "            </p>\n" +
                "          </div>\n" +
                "\n" +
                "          <!-- PROFESSIONAL SNAPSHOT -->\n" +
                "          <div class=\"section\">\n" +
                "            <h2>Professional Snapshot</h2>\n" +
                "            <ul class=\"timeline\">\n" +
                "              <li><strong>Total Experience:</strong> 5 years</li>\n" +
                "              <li><strong>Java Experience:</strong> 5 years</li>\n" +
                "              <li><strong>React Exposure:</strong> 6 months</li>\n" +
                "              <li><strong>Current CTC:</strong> 13 LPA</li>\n" +
                "              <li><strong>Preferred Location:</strong> Bangalore (open to relocate)</li>\n" +
                "              <li><strong>Highest Qualification:</strong> B.Tech in Computer Science</li>\n" +
                "              <li><strong>Notice Period:</strong> 60 days</li>\n" +
                "              <li><strong>Holding Offers:</strong> NO</li>\n" +
                "            </ul>\n" +
                "          </div>\n" +
                "\n" +
                "          <!-- CLOSING -->\n" +
                "          <div class=\"section\" style=\"background:#f9fafb;\">\n" +
                "            <p class=\"p\">\n" +
                "              I would be delighted to bring my expertise to your team.  \n" +
                "              Please find my <strong>resume attached</strong> for your kind review.  \n" +
                "              I look forward to the opportunity to discuss how my skills align with your needs.\n" +
                "            </p>\n" +
                "            <p class=\"p\">\n" +
                "              Warm regards,<br/>\n" +
                "              <strong>Sagar Srivastava</strong><br/>\n" +
                "             <a href=\"mob:6388660763\" style=\"color:#2563eb;\">6388660763</a>\n" +
                "            </p>\n" +
                "          </div>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String extractCompanyName(String email) {
        if (email == null || !email.contains("@")) return "";

        String domain = email.substring(email.indexOf("@") + 1); // e.g. "tcs.com"
        String company = domain.substring(0, domain.indexOf(".")); // e.g. "tcs"

        // Capitalize first letter
        return company.substring(0,1).toUpperCase() + company.substring(1).toLowerCase();
    }
        }
