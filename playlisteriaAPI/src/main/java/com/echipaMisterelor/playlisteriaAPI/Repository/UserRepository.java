package com.echipaMisterelor.playlisteriaAPI.Repository;

import com.echipaMisterelor.playlisteriaAPI.Model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsernameAndPassword(String username, String password);

    @Transactional
    void deleteUserByIdUser(Integer idUser);


}
