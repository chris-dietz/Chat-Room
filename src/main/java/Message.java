import java.security.SecureRandom;

public abstract class Message {
    protected final String from;
    protected final String subject;
    protected final String body;
    protected final String thread;
    protected final long msgId; //Unique message id set by server

    public Message(String from, String subject, String body, String thread) {
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.thread = thread;
        SecureRandom rng = new SecureRandom();
        msgId = rng.nextLong();
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getThread() {
        return thread;
    }

    public long getMsgId() {
        return msgId;
    }
}
