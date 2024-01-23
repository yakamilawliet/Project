package com.project.appointment.controller.domain;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String nickname;
    private String uid;
    private String openid;
}
