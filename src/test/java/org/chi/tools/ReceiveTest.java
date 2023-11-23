package org.chi.tools;

import org.chi.tools.dto.MessageDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.MessagingException;

import static org.chi.tools.cfg.Config.*;

public class ReceiveTest {

    @Test
    public static void receiveEmailTest() {


        String userName = "testacount831@gmail.com";
        String password =  "aoub whvr fdxp enif";

        Receiver receiver = new Receiver();
        MessageDto messageDto = null;
        try {
            messageDto = receiver.downloadLastEmail(IMAP_PROTOCOL, IMAP_HOST, IMAP_PORT, userName, password);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotNull(messageDto);
        Assert.assertTrue(messageDto.getMessage().length() > 0);

    }
}
