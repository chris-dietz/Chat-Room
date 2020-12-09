public class User {
    private final String name;
    private final String sessionCookie;

    public User(String name, String sessionCookie) {
        this.name = name;
        this.sessionCookie = sessionCookie;
    }

    public String getName() {
        return name;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }
}
