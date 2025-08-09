package model;

public class UserSession {

    public enum UserRole {
        GUEST, STUDENT, ADMIN
    }

    private static UserSession instance;


    private String userName;
    private UserRole role;

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

    public boolean isLoggedIn() {
        return role != UserRole.GUEST;
    }

    public void login(String name, UserRole userRole) {
        this.userName = name;
        this.role = userRole;
    }

    public void logout() {
        this.userName = null;
        this.role = UserRole.GUEST;
    }
}
