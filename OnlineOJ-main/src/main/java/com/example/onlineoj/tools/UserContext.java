package com.example.onlineoj.tools;

import com.example.onlineoj.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public static User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    public static String getCurrentUsername(HttpSession session) {
        User user = getCurrentUser(session);
        return user == null ? null : user.getName();
    }
}