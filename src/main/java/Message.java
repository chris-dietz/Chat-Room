import java.security.SecureRandom;

public abstract class Message {
    private final String from;
    private final String subject;
    private final String body;
    private final String thread;
    private long msgId = -1; //Unique message id set by server
    private final String type;

    public Message(String type,String from, String subject, String body, String thread) {
        this.type = type;
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.thread = thread;
        SecureRandom rng = new SecureRandom();
        msgId = rng.nextLong();
    }

    public void generateMsgID(){
        SecureRandom rng = new SecureRandom();
        msgId = Math.abs(rng.nextLong());
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
