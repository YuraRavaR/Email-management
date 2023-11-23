package org.chi.tools.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class MessageDto {

    String from;
    String to;
    String cc;
    String subject;
    Date sentDate;
    String message;

    public MessageDto() {
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public String getSubject() {
        return subject;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public String getMessage() {
        return message;
    }

    public MessageDto(String from, String to, String cc, String subject, Date sentDate, String message) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.sentDate = sentDate;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDto message1 = (MessageDto) o;
        return from.equals(message1.from) && to.equals(message1.to) && Objects.equals(subject, message1.subject) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, subject, message);
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cc='" + cc + '\'' +
                ", subject='" + subject + '\'' +
                ", sentDate=" + sentDate +
                ", message='" + message + '\'' +
                '}';
    }

}
