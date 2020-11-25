import java.security.SecureRandom;

public abstract class Message {
    protected final String to;
    protected final String from;
    protected final String subject;
    protected final String body;
    protected final String thread;
    protected final long msgId; //Unique message id set by server

    public Message(String to, String from, String subject, String body, String thread) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.thread = thread;
        SecureRandom rng = new SecureRandom();
        msgId = rng.nextLong();
    }






}
