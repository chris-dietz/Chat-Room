public class User {
    private final String name;
    private final String authCookie;

    public User(String name, String authCookie) {
        this.name = name;
        this.authCookie = authCookie;
    }

    public String getName() {
        return name;
    }

    public String getAuthCookie() {
        return authCookie;
    }
}
