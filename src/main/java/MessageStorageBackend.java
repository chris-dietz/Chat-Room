import java.util.ArrayList;
import java.util.List;

/**
 * High level abstraction for backend persistent storage,
 * For now implemented with non persistent storage, plan to migrate to
 * MySQL database in the future
 */

public class MessageStorageBackend {
    List<Message> messages;

    MessageStorageBackend(){
        messages = new ArrayList<>(128);
    }

    /*
     * Returns the last N messages posted to the chat room.
     */
    List<Message> getLastNMessages(int n){
        ArrayList<Message> toReturn = new ArrayList<>(n);
        if(n > messages.size()){ //Handle n being greater than the total number of messages by setting it to the max if it exceeds it.
            n=messages.size();
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
    List<Message> getMessagesPostedSince(long msg_id){
        ArrayList<Message> toReturn = new ArrayList<>();
        Message current = messages.get(messages.size()-1);
        int n = 0;
        while (n<messages.size() && current.getMsgId() != msg_id){
            current = messages.get(messages.size()-n);
            toReturn.add(current);
            n++;
        }
        return toReturn;
    }

    boolean insertMessage(Message m) {
        return messages.add(m);
    }

    Message removeMessage(long msg_id){
        for(Message m: messages){
            if(m.getMsgId() == msg_id){
                return m;
            }
        }
        return null;
    }



}
