public class GroupMessage extends Message{
    private final String room;
    public GroupMessage(String from, String subject, String body, String thread, String room) {
        super("group_message",from, subject, body, thread);
        this.room = room;
    }

    public String getRoom() {
        return room;
    }
}
