package com.teamname.plant.plant.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    private LocalDateTime createdAt;
}
