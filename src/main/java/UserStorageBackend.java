import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 * High level abstraction for backend persistent storage of user data,
 * For now implemented with non persistent storage, plan to migrate to
 * MySQL database in the future
 */

public class UserStorageBackend {
    //private final List<User> users;
    public UserStorageBackend(){
        //users = new ArrayList<>();

    }

    public void addUser(User u){
        try{
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/", "root","cs370minikube");
            Statement stmt = c.createStatement();
            String s = "USE chat_history";
            stmt.executeUpdate(s);
            String sql = "INSERT INTO user_history "
                    + "VALUES('" + u.getName() +"','" + u.getAuthCookie()+"')";
            stmt.executeUpdate(sql);
            c.close();
            stmt.close();
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

        //users.add(u);
    }

    /*
    retrieves a given user by name
     */

    public User getUserFromName(String name){
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        User toReturn = null;
        try{
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "cs370minikube");
            Statement s = c.createStatement();
            s.executeQuery("USE chat_history");
            preparedStatement = c.prepareStatement("SELECT * FROM user_history WHERE username=?");
            preparedStatement.setString(1,name);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                toReturn = new User(resultSet.getString("username"),resultSet.getString("usercookie"));
            }

            s.close();
            c.close();
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return toReturn;
    }

    public boolean isCookieValid(String name, String cookie){
        User toCheck = getUserFromName(name);
        if(toCheck == null){
            return false;
        }
        //System.out.println("Comparing :" + toCheck.getSessionCookie() + "to" + cookie);
        return toCheck.getAuthCookie().equals(cookie);
    }

}
