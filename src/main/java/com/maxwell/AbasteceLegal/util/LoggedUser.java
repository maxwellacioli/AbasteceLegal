package com.maxwell.AbasteceLegal.util;

import com.maxwell.AbasteceLegal.model.User;
import lombok.Getter;
import lombok.Setter;

public class LoggedUser {

    private static LoggedUser loggedUser;

    @Getter
    @Setter
    private User user;

    private LoggedUser() {}

    public static LoggedUser getInstance() {
        if(loggedUser == null) {
            loggedUser = new LoggedUser();
        }

        return loggedUser;
    }

}
