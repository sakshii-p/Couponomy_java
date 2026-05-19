// File: com.superx.Model.UserSession.java
package com.superx.Model;

public class UserSession {
    private static UserDetails currentUser;

    public static void setCurrentUser(UserDetails user) {
        currentUser = user;
    }

    public static UserDetails getCurrentUser() {
        return currentUser;
    }
}
