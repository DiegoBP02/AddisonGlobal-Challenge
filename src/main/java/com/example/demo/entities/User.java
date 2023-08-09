package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Credentials credentials;
}
