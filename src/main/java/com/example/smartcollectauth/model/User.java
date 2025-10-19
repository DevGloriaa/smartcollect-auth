package com.example.smartcollectauth.model;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "smart-collect")
public class User {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String fullName;

    @Email
    private String email;
    private String password;
}

