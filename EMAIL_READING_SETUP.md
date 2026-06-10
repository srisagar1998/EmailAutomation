# Automatic Email Reading from Gmail - Setup Guide

## Overview
Your Spring Boot application now has the capability to automatically read new emails from your Gmail inbox. The system will:
- Check for new unread emails every 30 seconds (configurable)
- Automatically mark emails as read after processing
- Extract email content (sender, subject, date, body)
- Provide both automatic and manual email reading

## Features Implemented

### 1. **Automatic Email Reading (Scheduled)**
- Runs automatically every 30 seconds in the background
- Checks Gmail IMAP for unread emails
- Processes and logs new emails to console
- Marks emails as read after processing

### 2. **Manual Email Reading (API Endpoint)**
- Endpoint: `GET /api/email/read-emails`
- Fetch new emails on-demand
- Returns email data in JSON format

### 3. **Automatic Email Sending (Existing)**
- Endpoint: `POST /api/email/send-emails`
- Bulk email sending with attachments

## Setup Instructions

### Step 1: Enable Gmail IMAP and App Passwords

Since you're using Gmail with app password authentication, follow these steps:

1. **Enable 2-Factor Authentication** (if not already enabled):
   - Go to https://myaccount.google.com/security
   - Click "2-Step Verification"
   - Complete the setup

2. **Generate App Password**:
   - Go to https://myaccount.google.com/apppasswords
   - Select "Mail" as the app
   - Select "Windows Computer" (or your device)
   - Click "Generate"
   - Copy the 16-character password

3. **Update application.properties**:
   ```properties
   spring.mail.password=<your-16-char-app-password>
   ```

### Step 2: Dependencies Added
The following dependencies have been added to `pom.xml`:
```xml
<!-- JavaMail API for reading emails -->
<dependency>
    <groupId>jakarta.mail</groupId>
    <artifactId>jakarta.mail-api</artifactId>
    <version>2.1.3</version>
</dependency>
<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>2.1.3</version>
</dependency>
```
 
### Step 3: Configuration in application.properties
Already configured in `src/main/resources/application.properties`:
```properties
# IMAP Configuration
mail.imap.host=imap.gmail.com
mail.imap.port=993
mail.imap.username=sagarsrivastava590@gmail.com
mail.imap.password=udgt iuyz diwp psic

# Schedule interval (milliseconds)
email.read.schedule.fixed-delay=30000
```

### Step 4: Start the Application
```bash
mvn spring-boot:run
```

Or build and run the JAR:
```bash
mvn clean package
java -jar target/work-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### 1. Send Emails (Existing)
**Endpoint**: `POST /api/email/send-emails`

**Request Body**:
```json
{
  "subject": "Hello",
  "mobileNo": "1234567890",
  "status": "pending",
  "emails": {
    "John Doe": "john@example.com",
    "Jane Smith": "jane@example.com"
  },
  "attachments": ["/path/to/file1.pdf", "/path/to/file2.pdf"]
}
```

**Response**:
```json
{
  "success": true,
  "message": "✅ Emails sent successfully!"
}
```

### 2. Read Emails (NEW - Manual)
**Endpoint**: `GET /api/email/read-emails`

**Response**:
```json
{
  "success": true,
  "message": "✅ Emails read successfully!",
  "count": 2,
  "emails": [
    {
      "from": "[Gmail Support <support@google.com>]",
      "subject": "Your Google Account security notification",
      "date": "2026-04-05T10:30:00Z",
      "content": "Someone signed in to your Google Account...",
      "timestamp": 1712315400000
    },
    {
      "from": "[noreply@github.com]",
      "subject": "You have a new notification",
      "date": "2026-04-05T09:15:00Z",
      "content": "GitHub notification...",
      "timestamp": 1712311500000
    }
  ]
}
```

## How It Works

### Automatic Email Reading Flow
1. **Scheduled Task**: `@Scheduled(fixedDelay = 30000)` runs every 30 seconds
2. **IMAP Connection**: Connects to Gmail IMAP server using SSL/TLS
3. **Search Unread**: Searches for unread emails using IMAP search criteria
4. **Extract Data**: Extracts sender, subject, date, and content
5. **Mark as Read**: Flags emails as read to avoid reprocessing
6. **Close Connection**: Closes IMAP connection properly
7. **Log Output**: Prints processed emails to console (configurable to database)

### Code Structure
```
AutoEmailRead.java
├── readEmailsAutomatically()      // Scheduled task
├── fetchNewEmails()                // Connect & fetch emails
├── extractEmailData()              // Parse email message
├── extractContent()                // Get email body
├── stripHtmlTags()                 // Clean HTML content
└── getNewEmails()                  // Manual API method
```

## Configuration Options

### Change Email Check Interval
In `application.properties`, adjust `email.read.schedule.fixed-delay`:
```properties
# Every 10 seconds
email.read.schedule.fixed-delay=10000

# Every 1 minute
email.read.schedule.fixed-delay=60000

# Every 5 minutes
email.read.schedule.fixed-delay=300000
```

Or modify directly in `AutoEmailRead.java`:
```java
@Scheduled(fixedDelay = 60000)  // Change 30000 to desired milliseconds
public void readEmailsAutomatically() {
    // ...
}
```

## Logging and Monitoring

The application logs email reading activity to console:
```
🔄 Starting automatic email read...
📧 Found 3 new email(s)
✅ Email from: [sender@example.com]
   Subject: Test Email
   Date: Sun Apr 05 10:30:00 UTC 2026
   Content: This is a test email...
---
```

## Troubleshooting

### Error: "javax.mail.AuthenticationFailedException"
**Solution**: 
- Verify Gmail username and password are correct
- Use an App Password instead of Gmail password
- Enable "Less secure app access" is NOT recommended (use App Password instead)

### Error: "javax.mail.MessagingException: [IMAP] Connection refused"
**Solution**:
- Check internet connection
- Ensure Gmail IMAP is enabled in account settings
- Verify IMAP port 993 is not blocked by firewall

### No emails are being read
**Solution**:
- Check that you have unread emails in your Gmail inbox
- Verify the scheduled task is running (check console logs)
- Check Gmail IMAP is enabled

### High CPU Usage
**Solution**:
- Increase the `fixedDelay` value in the scheduler
- Current: 30 seconds (30000ms) - very frequent
- Recommended: 300000ms (5 minutes) for production

## Security Notes

⚠️ **Important Security Recommendations**:

1. **Never use plain passwords in application.properties for production**
   - Use environment variables
   - Use Spring Cloud Config
   - Use AWS Secrets Manager or similar

2. **Use Gmail App Passwords (not your main password)**
   - More secure than using your actual Gmail password
   - Can be revoked independently

3. **Enable 2FA on your Gmail account**
   - Protects your Gmail account
   - Required for generating App Passwords

4. **Limit access to sensitive files**
   - Ensure application.properties has restricted file permissions
   - Don't commit credentials to version control

## Production Deployment

For production environment:

1. **Use environment variables** instead of hardcoded credentials:
```bash
export SPRING_MAIL_USERNAME=your-email@gmail.com
export SPRING_MAIL_PASSWORD=your-app-password
```

2. **Adjust scheduler interval** based on requirements:
```properties
# Production: Check every 5 minutes (reduces load)
email.read.schedule.fixed-delay=300000
```

3. **Add error handling and notifications** when emails fail to read

4. **Monitor email reading performance** and logs

## Next Steps (Optional Enhancements)

1. **Save emails to database**:
   - Create EmailReadEntity to store fetched emails
   - Add database persistence in AutoEmailRead

2. **Filter emails by criteria**:
   - Filter by sender domain
   - Filter by keywords in subject
   - Filter by date range

3. **Send notifications**:
   - Send SMS when specific emails arrive
   - Send Slack/Teams notifications
   - Send webhooks to other services

4. **Add email attachments handling**:
   - Extract and save attachments
   - Process attachment files

5. **Advanced search**:
   - Search by from address
   - Search by subject keywords
   - Search by date range

## Support

For issues or questions about the email reading implementation, check:
- Spring Mail documentation: https://spring.io/guides/gs/sending-email/
- Jakarta Mail documentation: https://eclipse-ee4j.github.io/mail/
- Gmail IMAP setup: https://support.google.com/mail/answer/7126229

---
**Setup completed!** Your application now reads Gmail automatically. ✅

