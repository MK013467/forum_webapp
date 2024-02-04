package org.example.ssnproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "user_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private String password;

    @Column(unique = true, nullable = false)
    private String email;
}
