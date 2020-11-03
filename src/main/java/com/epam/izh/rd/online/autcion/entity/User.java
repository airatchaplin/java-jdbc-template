package com.epam.izh.rd.online.autcion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Пользователь аукциона
 */
@Data
public class User {
    private Long userId;
    private String billingAddress;
    private String fullName;
    private String login;
    private String password;

    public User(Long userId, String billingAddress, String fullName, String login, String password) {
        this.userId = userId;
        this.billingAddress = billingAddress;
        this.fullName = fullName;
        this.login = login;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
