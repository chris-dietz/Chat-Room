import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public abstract class Message {
    private final String from;
    private final String subject;
    private final String body;
    private final String thread;
    private final long msgId;//Unique message id set by server
    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private final String type;
    private final String timestamp;

    public Message(String type,String from, String subject, String body, String thread) {
        this.type = type;
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.thread = thread;
        SecureRandom rng = new SecureRandom();
        msgId = Math.abs(rng.nextLong());
        timestamp = ZonedDateTime.now( ZoneOffset.UTC ).truncatedTo( ChronoUnit.SECONDS).format( DateTimeFormatter.ISO_INSTANT );
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
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
