package org.chi.tools;

import org.chi.tools.cfg.Config;
import org.chi.tools.dto.MessageDto;
import org.chi.tools.util.Utils;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Receiver {



    /**
     * Downloads new messages and fetches details for each message.
     * @param protocol
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    public MessageDto downloadLastEmail(String protocol, String host, String port,
                               final String userName, final String password) throws MessagingException {
        Properties properties = Utils.getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
        Store store = null;
        Folder folderInbox = null;
        try {
            // connects to the message store
            store = session.getStore(protocol);
            store.connect(userName, password);

            // opens the inbox folder
            folderInbox = store.getFolder(Config.FOLDER);
            folderInbox.open(Folder.READ_ONLY);

            // fetches last message from server
            Message[] messages = folderInbox.getMessages();
            Message msg = messages[messages.length - 1];
            return parseMessage(msg);

        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } finally {
            // disconnect
            if(folderInbox != null)
                folderInbox.close(false);
            if(store != null)
                store.close();
        }

        return new MessageDto();
    }


    public List<MessageDto> downloadEmails(String protocol, String host, String port,
                                           final String userName, final String password) throws MessagingException {
        Properties properties = Utils.getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
        Store store = null;
        Folder folderInbox = null;
        List<MessageDto> messageDtoList = new ArrayList<>();
        try {
            // connects to the message store
            store = session.getStore(protocol);
            store.connect(userName, password);

            // opens the inbox folder
            folderInbox = store.getFolder(Config.FOLDER);
            folderInbox.open(Folder.READ_ONLY);

            // fetches all messages from server
            Message[] messages = folderInbox.getMessages();
            for (Message msg : messages) {
                MessageDto messageDto = parseMessage(msg);
                messageDtoList.add(messageDto);
            }

        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } finally {
            // disconnect
            if(folderInbox != null)
                folderInbox.close(false);
            if(store != null)
                store.close();
        }

        return messageDtoList;
    }

//    private MessageDto parseMessage(Message msg) throws MessagingException {
//        Address[] fromAddress = msg.getFrom();
//        String from = fromAddress[0].toString();
//        String subject = msg.getSubject();
//        String toList = parseAddresses(msg
//                .getRecipients(Message.RecipientType.TO));
//        String ccList = parseAddresses(msg
//                .getRecipients(Message.RecipientType.CC));
//
//        String contentType = msg.getContentType();
//        String messageContent = "";
//
//        if (contentType.contains("text/plain")
//                || contentType.contains("text/html")) {
//            try {
//                Object content = msg.getContent();
//                if (content != null) {
//                    messageContent = content.toString();
//                }
//            } catch (Exception ex) {
//                messageContent = "[Error downloading content]";
//                ex.printStackTrace();
//            }
//        }
//        return new MessageDto(from, subject, toList, ccList, msg.getSentDate(), messageContent);
//    }
private MessageDto parseMessage(Message msg) throws MessagingException {
    Address[] fromAddress = msg.getFrom();
    String from = fromAddress[0].toString();
    String subject = msg.getSubject();
    String toList = parseAddresses(msg.getRecipients(Message.RecipientType.TO));
    String ccList = parseAddresses(msg.getRecipients(Message.RecipientType.CC));

    String contentType = msg.getContentType();
    String messageContent = "";

    try {
        if (msg.isMimeType("text/plain")) {
            messageContent = (String) msg.getContent();
        } else if (msg.isMimeType("text/html")) {
            // Handle HTML content if needed
            messageContent = (String) msg.getContent();
        } else if (msg.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) msg.getContent();
            messageContent = extractTextFromMultipart(multipart);
        }
    } catch (IOException ex) {
        messageContent = "[Error downloading content]";
        ex.printStackTrace();
    }

    return new MessageDto(from, subject, toList, ccList, msg.getSentDate(), messageContent);
}
    private String extractTextFromMultipart(Multipart multipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();

        int count = multipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String disposition = bodyPart.getDisposition();
            if (disposition != null && disposition.equalsIgnoreCase("ATTACHMENT")) {
                // Handle attachment if needed
            } else {
                result.append(getTextFromBodyPart(bodyPart));
            }
        }

        return result.toString();
    }

    private String getTextFromBodyPart(BodyPart bodyPart) throws MessagingException, IOException {
        if (bodyPart.isMimeType("text/plain")) {
            return (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            // Handle HTML content if needed
            return (String) bodyPart.getContent();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            // Recursive call for nested multiparts
            MimeMultipart multipart = (MimeMultipart) bodyPart.getContent();
            return extractTextFromMultipart(multipart);
        }

        return "";
    }


    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        StringBuilder listAddress = new StringBuilder();

        if (address != null) {
            for (Address value : address) {
                listAddress.append(value.toString()).append(", ");
            }
        }
        if (listAddress.length() > 1) {
            listAddress = new StringBuilder(listAddress.substring(0, listAddress.length() - 2));
        }

        return listAddress.toString();
    }
}
