package model;

public class UserSession {

    public enum UserRole {
        GUEST, STUDENT, ADMIN
    }

    private static UserSession instance;


    private String userName;
    private UserRole role;
    private int Id;

    private UserSession() {

        this.userName = null;
        this.role = UserRole.GUEST;
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public UserRole getRole() {
        return role;
    }

    public int getId() {
        return Id;
    }
    public boolean isLoggedIn() {
        return role != UserRole.GUEST;
    }

    public void login(int Id,String name, UserRole userRole) {
        this.userName = name;
        this.role = userRole;
        this.Id = Id;
    }

    public void logout() {
        this.userName = null;
        this.role = UserRole.GUEST;
    }
}
