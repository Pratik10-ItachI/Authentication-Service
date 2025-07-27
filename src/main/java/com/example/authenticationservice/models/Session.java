package com.example.authenticationservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Entity
@Getter
@Setter
public class Session extends BaseModel {
    private String token;
    @OneToOne
    private User user;
    private Date expireAt;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus status;
}
