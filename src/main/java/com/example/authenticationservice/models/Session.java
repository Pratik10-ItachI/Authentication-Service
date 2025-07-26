package com.example.authenticationservice.models;

import jakarta.persistence.*;


import java.util.Date;

@Entity
public class Session extends BaseModel {
    private String token;
    @OneToOne
    private User user;
    private Date expireAt;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus status;
}
