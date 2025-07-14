package com.example.onlineoj.model;


import lombok.Data;

@Data
public class User {
    private String name;
    private String password;
    private Integer role;//1:user 0:admin
    private Integer id;
    private Integer allnumber;
    private Integer acnumber;
}
