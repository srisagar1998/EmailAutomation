package com.uitility.personal.work.Service;

import com.uitility.personal.work.modal.EmailRead;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.mail.*;
import jakarta.mail.event.MessageCountAdapter;
import jakarta.mail.event.MessageCountEvent;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import com.sun.mail.imap.IMAPFolder;
import org.eclipse.angus.mail.imap.IMAPFolder;

import java.util.Properties;

@Service
public class AutoEmailReadService {
// java

        @Value("${spring.mail.imap.host}")
        private String host;

        @Value("${spring.mail.imap.port:993}")
        private int port;

        @Value("${spring.mail.imap.username}")
        private String username;

        @Value("${spring.mail.imap.password}")
        private String password;

        @Value("${spring.mail.properties.mail.imap.ssl.enable:true}")
        private boolean ssl;

        @Value("${spring.mail.imap.folder:INBOX}")
        private String folderName;
        @Autowired
        private AutoReplyService autoReplyService;

        @Autowired
        AIReplyService AIReplyService;

        private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        private volatile boolean running = true;
        private Store store;
        private IMAPFolder folder;

        @PostConstruct
        public void start() {
            executor.submit(this::idleLoop);
        }

        @PreDestroy
        public void stop() {
            running = false;
            executor.shutdownNow();
            closeResources();
        }

        public List<Map<String, Object>> idleLoop() {
            int backoffSeconds = 1;
            List<Map<String, Object>> result =  new ArrayList<>();
            while (running) {
                try {
                    ensureConnected();
                    if (folder != null && folder.isOpen()) {
                        folder.idle(); // blocks until server notifies or timeout
                    } else {
                        Thread.sleep(1000);
                    }
                    backoffSeconds = 1; // reset backoff after successful idle
                } catch (MessagingException | InterruptedException ex) {
                    closeResources();
                    if (!running) break;
                    try {
                        TimeUnit.SECONDS.sleep(backoffSeconds);
                    } catch (InterruptedException ignored) { break; }
                    backoffSeconds = Math.min(backoffSeconds * 2, 60);
                } catch (Exception ex) {
                    // unexpected error: close & backoff
                    closeResources();
                    try {
                        TimeUnit.SECONDS.sleep(Math.min(backoffSeconds, 60));
                    } catch (InterruptedException ignored) { break; }
                    backoffSeconds = Math.min(backoffSeconds * 2, 60);
                }
            }
            closeResources();
            return List.of(); // or return collected emails if you implement that
        }

        private synchronized void ensureConnected() throws MessagingException {
            if (store != null && store.isConnected() && folder != null && folder.isOpen()) {
                return;
            }
            closeResources();

            Properties props = new Properties();
            if (ssl) {
                props.setProperty("mail.imap.ssl.enable", "true");
                props.setProperty("mail.imap.ssl.trust", "*");
            }
            props.setProperty("mail.imap.starttls.enable", "true");
            props.setProperty("mail.imap.timeout", "60000");
            props.setProperty("mail.imap.connectiontimeout", "60000");

            Session session = Session.getInstance(props);
            store = session.getStore("imap");
            store.connect(host, port, username, password);

            Folder f = store.getFolder(folderName);
            folder = (IMAPFolder) f;
            folder.open(Folder.READ_WRITE);

            folder.addMessageCountListener(new MessageCountAdapter() {
                @Override
                public void messagesAdded(MessageCountEvent ev) {
                    Message[] msgs = ev.getMessages();
                    for (Message msg : msgs) {
                        try {
                            EmailRead item = toEmailRead(msg);
                            // handle the new message (e.g., push to application queue, log, etc.)
                            System.out.println("New mail: " + item);
                            // mcp server can call autoReplyService here to send an auto-reply if needed
                            String AIReply = AIReplyService.generateReply(item.getBody());
                            autoReplyService.sendAutoReply(msg,/*"Thank you for your email. We have received your message and will get back to you shortly."*/AIReply);
                            // optionally fetch flags/body if needed
                        } catch (Exception e) {
                            // handle parsing error
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        private EmailRead toEmailRead(Message msg) throws MessagingException, IOException {
            String subject = msg.getSubject();
            String from = null;
            Address[] fromAddrs = msg.getFrom();
            if (fromAddrs != null && fromAddrs.length > 0) {
                from = ((InternetAddress) fromAddrs[0]).toUnicodeString();
            }

            String to = addressesToString(msg.getRecipients(Message.RecipientType.TO));
            System.out.println("To: " + to);
            String cc = addressesToString(msg.getRecipients(Message.RecipientType.CC));
            System.out.println("CC: " + cc);
            String bcc = addressesToString(msg.getRecipients(Message.RecipientType.BCC));
            System.out.println("BCC: " + bcc);

            String body = extractText(msg);
            System.out.println("body: " + body);

            return new EmailRead(subject, body, cc, bcc, from, to);
        }

        private String addressesToString(Address[] addrs) {
            if (addrs == null || addrs.length == 0) return null;
            StringBuilder sb = new StringBuilder();
            for (Address a : addrs) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(((InternetAddress) a).toUnicodeString());
            }
            return sb.toString();
        }

        private String extractText(Part p) throws MessagingException, IOException {
            if (p.isMimeType("text/*")) {
                Object content = p.getContent();
                return content == null ? null : content.toString();
            }
            if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    String s = extractText(mp.getBodyPart(i));
                    if (s != null && !s.isBlank()) return s;
                }
            }
            return null;
        }

        private synchronized void closeResources() {
            try {
                if (folder != null && folder.isOpen()) folder.close(false);
            } catch (Exception ignored) {}
            try {
                if (store != null && store.isConnected()) store.close();
            } catch (Exception ignored) {}
            folder = null;
            store = null;
        }
    }

