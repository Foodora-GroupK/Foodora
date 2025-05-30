public abstract class User {
    protected String id;
    protected String name;
    protected String username;
    protected String password;

    public User(String id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
}