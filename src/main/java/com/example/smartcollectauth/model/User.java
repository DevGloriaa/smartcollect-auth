package com.example.smartcollectauth.model;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "smart-collect")
public class User {
    @Id
    private String id;

    private String fullName;
    private String email;
    private String password;
}

