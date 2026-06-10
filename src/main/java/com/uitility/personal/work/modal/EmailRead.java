package com.uitility.personal.work.modal;

import java.io.Serializable;
import java.util.Objects;
public class EmailRead implements Serializable {

        private static final long serialVersionUID = 1L;

        private String subject;
        private String body;
        private String cc;
        private String bcc;
        private String from;
        private String to;

        public EmailRead() {
        }

        public EmailRead(String subject, String body, String cc, String bcc, String from, String to) {
            this.subject = subject;
            this.body = body;
            this.cc = cc;
            this.bcc = bcc;
            this.from = from;
            this.to = to;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }

        public String getBcc() {
            return bcc;
        }

        public void setBcc(String bcc) {
            this.bcc = bcc;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        @Override
        public String toString() {
            return "EmailRead{" +
                    "subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    ", cc='" + cc + '\'' +
                    ", bcc='" + bcc + '\'' +
                    ", from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EmailRead emailRead = (EmailRead) o;
            return Objects.equals(subject, emailRead.subject) &&
                    Objects.equals(body, emailRead.body) &&
                    Objects.equals(cc, emailRead.cc) &&
                    Objects.equals(bcc, emailRead.bcc) &&
                    Objects.equals(from, emailRead.from) &&
                    Objects.equals(to, emailRead.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subject, body, cc, bcc, from, to);
        }
    }
    
