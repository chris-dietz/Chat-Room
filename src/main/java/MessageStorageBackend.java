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
    private long nextMsgId;
    private final String username;
    private final String password;
    private final String url;

    public MessageStorageBackend(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
        nextMsgId = 0;
        createDatabase();
        nextMsgId = getLastID();


    }

    private long getLastID(){
        long lastID = 0;
        try {
            Connection c = DriverManager.getConnection(url, username,password);
            ///String sql = "INSERT INTO message_history(mfrom, msubject, mbody, mthread, msgId, mtype, mtimestamp, mroom)";
            Statement s  = c.createStatement();
            s.executeUpdate("USE chat_history");
            ResultSet isEmpty = s.executeQuery("SELECT EXISTS (SELECT 1 FROM message_history);");
            isEmpty.next();
            int empt = isEmpty.getInt(1);
            if(empt == 1) {
                ResultSet r = s.executeQuery("SELECT * FROM message_history ORDER BY msgId DESC LIMIT 1");
                r.next();
                lastID = Long.parseLong(r.getString("msgId"));
            }
            else {
                lastID = 0;
            }
            s.close();
            c.close();

        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return lastID +1;
    }

    /*
     * Returns the last N messages posted to the chat room.
     *
     */
    public List<Message> getLastNMessages(int n){
        ArrayList<Message> toReturn = new ArrayList<>(n);

        String sqlQuery = " SELECT * FROM message_history ORDER BY msgId DESC LIMIT "+n+";";
        ResultSet results;
        try{
            Connection c = DriverManager.getConnection(url,username, password);
            Statement s = c.createStatement();
            s.executeQuery("USE chat_history");
            results =  s.executeQuery(sqlQuery);
            retrieveFromQuery(results,toReturn);
            s.close();
            c.close();
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }




        return toReturn;
    }

    private void retrieveFromQuery(ResultSet results, ArrayList<Message> result_list) throws SQLException{
        while (results.next()){
            String from = results.getString("mfrom");
            String subject = results.getString("msubject");
            String body = results.getString("mbody");
            String thread = results.getString("mthread");
            String msgId = results.getString("msgId");
            String type = results.getString("mtype");
            String timestamp = results.getString("mtimestamp");
            String room = results.getString("mroom");
            //String type,String from, String subject, String body, String thread,String room, long msgId, String timestamp
            Message m = new Message(type,from,subject,body,thread,room,Long.parseLong(msgId),timestamp);
            result_list.add(m);
        }
    }

    /*
     * Returns all messages posted since a specified message id
     * If the message id doesn't exist it returns nothing
     * Excludes message with matching ID
     */
   public List<Message> getMessagesPostedSince(long msg_id){
       ArrayList<Message> toReturn = new ArrayList<>();
       String sqlQuery = "SELECT * FROM message_history WHERE msgId BETWEEN "+msg_id+" AND "+ (nextMsgId-1) + ";";
       ResultSet results;
       try{
           Connection c = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "cs370minikube");
           Statement s = c.createStatement();
           s.executeQuery("USE chat_history");
           //System.out.println("Executing: "+ sqlQuery);
           results =  s.executeQuery(sqlQuery);
           retrieveFromQuery(results,toReturn);
           s.close();
           c.close();
       }catch (SQLException e){
           System.out.println("SQLException: " + e.getMessage());
           System.out.println("SQLState: " + e.getSQLState());
           System.out.println("VendorError: " + e.getErrorCode());
       }
        return  toReturn;
    }

    public void insertMessage(Message m) {

        try {
            Connection c = DriverManager.getConnection(url,username, password);
            ///String sql = "INSERT INTO message_history(mfrom, msubject, mbody, mthread, msgId, mtype, mtimestamp, mroom)";
            Statement s  = c.createStatement();
            String usedb = "USE chat_history";
            s.executeUpdate(usedb);
            String nothing = "nothing";
            String str = "INSERT INTO message_history "
                    + "VALUES('" + m.getFrom() + "','"+ m.getSubject() + "','"+ m.getBody() + "','"+ nothing + "','"+ m.getMsgId() + "','"+ m.getType() + "','"+ m.getTimestamp() + "','"+nothing +"')";
            s.executeUpdate(str);
            s.close();
            c.close();

        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
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

    public void createDatabase(){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, username,password);
            stmt = conn.createStatement();
            //String rmprevdb = "DROP DATABASE chat_history";
            //stmt.executeUpdate(rmprevdb);
            String makedb = "CREATE DATABASE IF NOT EXISTS chat_history";
            stmt.executeUpdate(makedb);
            String usedb = "USE chat_history";
            stmt.executeUpdate(usedb);
            String makemessagetable = "CREATE TABLE IF NOT EXISTS message_history(mfrom TEXT, msubject TEXT, mbody TEXT, mthread TEXT, msgId LONG, mtype TEXT, mtimestamp TEXT, mroom TEXT)";
            stmt.executeUpdate(makemessagetable);
            String makeusertable = "CREATE TABLE IF NOT EXISTS user_history(username TEXT, usercookie TEXT)";
            stmt.executeUpdate(makeusertable);
            stmt.close();
            conn.close();
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
