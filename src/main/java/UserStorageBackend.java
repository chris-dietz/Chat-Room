import java.util.ArrayList;
import java.util.List;

/**
 * High level abstraction for backend persistent storage of user data,
 * For now implemented with non persistent storage, plan to migrate to
 * MySQL database in the future
 */

public class UserStorageBackend {
    private final List<User> users;
    public UserStorageBackend(){
        users = new ArrayList<>();
    }

    public void addUser(User u){
        users.add(u);
    }

    /*
    retrieves a given user by name
     */

    public User getUserFromName(String name){
        for(User u: users){
            if(u.getName().equals(name)){
                return u;
            }
        }
        return null;
    }

    public boolean isCookieValid(String name, String cookie){
        User toCheck = getUserFromName(name);
        if(toCheck == null){
            return false;
        }
        //System.out.println("Comparing :" + toCheck.getSessionCookie() + "to" + cookie);
        return toCheck.getSessionCookie().equals(cookie);
    }

}
