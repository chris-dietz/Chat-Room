import java.util.ArrayList;
import java.util.List;

/**
 * High level abstraction for backend persistent storage,
 * For now implemented with non persistent storage, plan to migrate to
 * MySQL database in the future
 */

public class MessageStorageBackend {
    private final List<Message> messages;
    private long nextMsgId;
    public MessageStorageBackend(){
        messages = new ArrayList<>(128);
        nextMsgId = 0;
    }

    /*
     * Returns the last N messages posted to the chat room.
     */
   public List<Message> getLastNMessages(int n){
        ArrayList<Message> toReturn = new ArrayList<>(n);
        if(n > messages.size()){ //Handle n being greater than the total number of messages by setting it to the max if it exceeds it.
            n=messages.size();
        }

        if(messages.size() == 0){
            return toReturn;
        }

        for(int i = 1; i<= n ; i++){
            toReturn.add(messages.get(messages.size()-i));
        }

        return toReturn;
    }

    /*
     * Returns all messages posted since a specified message id
     * If the message id doesn't exist it returns all messages
     *
     */
   public List<Message> getMessagesPostedSince(long msg_id){
        ArrayList<Message> toReturn = new ArrayList<>();
        if(messages.size() == 0){
            return toReturn;
        }

        Message current = messages.get(messages.size()-1);
        int n = 0;
        while (n<messages.size() && current.getMsgId() != msg_id){
            current = messages.get(messages.size()-1-n);
            toReturn.add(current);
            n++;
        }
        return toReturn;
    }

    public boolean insertMessage(Message m) {
        return messages.add(m);
    }

    public Message getMessage(long msg_id){
        for(Message m: messages){
            if(m.getMsgId() == msg_id){
                return m;
            }
        }
        return null;
    }

    public Message removeMessage(long msg_id){
        for(Message m: messages){
            if(m.getMsgId() == msg_id){
                messages.remove(m);
                return m;
            }
        }
        return null;
    }

    public long getNextMsgId(){
        long currentMsgId = nextMsgId;
        nextMsgId++;
        return currentMsgId;
    }


}
