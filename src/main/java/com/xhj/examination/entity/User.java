package com.xhj.examination.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String identity;
    private String name;
    private String sex;
    private String phone;
    private String number;
    private String college;
    private String password;
}
