package com.masonord.harmonyhound.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {
    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private Long apiCalls;
}
