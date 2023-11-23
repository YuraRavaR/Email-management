package org.chi.tools.cfg;

public class Config {

    private Config() {
    }

    // for POP3
    public static final String POP3_PROTOCOL = "pop3";
    public static final String POP3_HOST = "pop.gmail.com";
    public static final String POP3_PORT = "995";

    // for IMAP
    public static final String IMAP_PROTOCOL = "imap";
    public static final String IMAP_HOST = "imap.gmail.com";
    public static final String IMAP_PORT = "993";

    public static final String FOLDER = "INBOX";
}
