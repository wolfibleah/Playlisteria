package com.echipaMisterelor.playlisteriaAPI.Model;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private Integer idUser;
    private String username;
    private String email;
    private String password;
    @Column(name="is_admin")
    private Boolean isAdmin;
}


