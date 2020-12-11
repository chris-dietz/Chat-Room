import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 * High level abstraction for backend persistent storage,
 * For now implemented with non persistent storage, plan to migrate to
 * MySQL database in the future
 */
// This is coles comment
public class MessageStorageBackend {
    private final List<Message> messages;
    private long nextMsgId;
    Connection connection = null;
    public MessageStorageBackend(){
        messages = new ArrayList<>(128);
        nextMsgId = 0;
        connection = createDatabase();
    }

    /*
     * Returns the last N messages posted to the chat room.
     *
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
     * If the message id doesn't exist it returns nothing
     * Excludes message with matching ID
     */
   public List<Message> getMessagesPostedSince(long msg_id){
        ArrayList<Message> toReturn = new ArrayList<>();
        if(messages.size() == 0){
            return toReturn;
        }
        Message current = messages.get(messages.size()-1);
        int n = 1;
        while (n<messages.size() && current.getMsgId() != msg_id){
            toReturn.add(current);
            current = messages.get(messages.size()-1-n);
            n++;
        }

        if(current.getMsgId() != msg_id){
            return new ArrayList<>();
        }

        //toReturn.remove(toReturn.size()-1);
        return toReturn;
    }

    public boolean insertMessage(Message m) {
       Connection c = connection;
        try {
            String sql = "INSERT INTO message_history(mfrom, msubject, mbody, mthread, msgId, mtype, mtimestamp, mroom)";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1, m.getFrom());
            statement.setString(2, m.getSubject());
            statement.setString(3, m.getBody());
            statement.setString(4, "nothing"); // Null because we did not end up using data field
            statement.setLong(5, m.getMsgId());
            statement.setString(6, m.getType());
            statement.setString(7, m.getTimestamp());
            statement.setString(8, "nothing"); // Null because we did not end up using data field
            c.close();
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
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

    /**
     * Returns the next sequential Message id and increments nextMsgId
     * Should NEVER return the same number twice.
     * @return Next Message id.
     */
    public long getNextMsgId(){
        long currentMsgId = nextMsgId;
        nextMsgId++;
        return currentMsgId;
    }

    public Connection createDatabase(){
        Connection conn = null;
        Statement stmt = null;
        try {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root","cs370minikube");
        stmt = conn.createStatement();
        String makedb = "CREATE DATABASE chat_history";
        stmt.executeUpdate(makedb);
        String usedb = "USE chat_history";
        stmt.executeUpdate(usedb);
        String makemessagetable = "CREATE TABLE message_history(mfrom TEXT, msubject TEXT, mbody TEXT, mthread TEXT, msgId LONG, mtype TEXT, mtimestamp TEXT, mroom TEXT)";
        stmt.executeUpdate(makemessagetable);
        String makeusertable = "CREATE TABLE user_history(username TEXT, usercookie TEXT)";
        stmt.executeUpdate(makeusertable);
        stmt.close();
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return conn;
    }


}
