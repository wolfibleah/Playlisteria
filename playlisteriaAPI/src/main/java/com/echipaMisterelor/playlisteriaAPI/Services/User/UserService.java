package com.echipaMisterelor.playlisteriaAPI.Services.User;

import com.echipaMisterelor.playlisteriaAPI.Controller.UserController;
import com.echipaMisterelor.playlisteriaAPI.Exceptions.InvalidInputException;
import com.echipaMisterelor.playlisteriaAPI.Exceptions.ResourceNotFoundException;
import com.echipaMisterelor.playlisteriaAPI.Model.ActionResult;
import com.echipaMisterelor.playlisteriaAPI.Model.LoginResponse;
import com.echipaMisterelor.playlisteriaAPI.Model.User;
import com.echipaMisterelor.playlisteriaAPI.Repository.StatisticsRepository;
import com.echipaMisterelor.playlisteriaAPI.Repository.UserRepository;
import com.echipaMisterelor.playlisteriaAPI.View.UserCredentials;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    // User management

    public Optional<User> getUserByIdUser(Integer idUser) {
        return userRepository.findById(idUser);
    }

    public User updateUser(User user, Integer oldIdUser) {
        User existingUser = userRepository.findById(oldIdUser)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + oldIdUser));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    public List<User> getAllUsers() {
        Iterable<User> userIterable = userRepository.findAll();
        return StreamSupport.stream(userIterable.spliterator(), false).collect(Collectors.toList());
    }

    public Boolean deleteUser(Integer idUser) {
        if(getUserByIdUser(idUser).isPresent()) {
            userRepository.deleteById(idUser);
            return true;
        }
        else
            return false;
    }

    // Authentication and authorization

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$";
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]+$");
    }

    public ActionResult isValidRegistrationInfo(User user){
        // check if email and username have valid format
        if (!isValidEmail(user.getEmail()))
            return new ActionResult(false, "Invalid email");
        if (!isValidUsername(user.getUsername()))
            return new ActionResult(false, "Invalid username");

        // check if the email already exists
        if(userRepository.findUserByEmail(user.getEmail()).isPresent())
            return new ActionResult(false, "Email already exists");
        // check if the username already exists
        if(userRepository.findUserByUsername(user.getUsername()).isPresent())
            return new ActionResult(false, "Username already exists");

        return new ActionResult(true, "Success");
    }

    public ActionResult isValidLoginInfo(UserCredentials user) throws InvalidInputException{
        if(userRepository.findUserByUsernameAndPassword(user.getUsername(), user.getPassword()).isEmpty())
            return new ActionResult(false, "Invalid username or password");

        return new ActionResult(true, "Success");
    }

    public LoginResponse registerUser(User user) {
        ActionResult result = isValidRegistrationInfo(user);

        if(result.isSuccessful()) {
            userRepository.save(user);
            // Get authentication token

            String token = Jwts.builder()
                    .issuer("Playlisteria")
                    .subject(user.getUsername())
                    .issuedAt(new Timestamp(System.currentTimeMillis()))
                    .expiration(new Timestamp(System.currentTimeMillis() + 6000000))
                    .signWith(SignatureAlgorithm.HS256, "MyVeryCoolAndVerySecretivePasswordThatWillNeverBeFound".getBytes())
                    .compact();

            return new LoginResponse(token, "Success");
        }
        else
        {
            return new LoginResponse("", result.getMessage());
        }

    }

    public LoginResponse loginUser(UserCredentials user){
        ActionResult result = isValidLoginInfo(user);
        if(result.isSuccessful()){
            //get user id
            Integer userId = userRepository.findUserByUsernameAndPassword(user.getUsername(), user.getPassword()).get().getIdUser();

            String token = Jwts.builder()
                    .issuer("Playlisteria")
                    .subject(userId.toString())
                    .issuedAt(new Timestamp(System.currentTimeMillis()))
                    .expiration(new Timestamp(System.currentTimeMillis() + 6000000))
                    .signWith(SignatureAlgorithm.HS256, "MyVeryCoolAndVerySecretivePasswordThatWillNeverBeFound".getBytes())
                    .compact();

            return new LoginResponse(token, "Success");
        }
        else
        {
            return new LoginResponse("", result.getMessage());
        }
    }

    public String getIdFromToken(String token){
        // get subject from token
        if (token != null && !token.isEmpty()) {
            Jws<Claims> claimsJws = null;

            try {
                claimsJws = Jwts.parser()
                        .setSigningKey("MyVeryCoolAndVerySecretivePasswordThatWillNeverBeFound".getBytes())
                        .build().parseSignedClaims(token);
            } catch (Exception e) {
                return "";
            }

            return claimsJws.getBody().getSubject();
        }

        return "";
    }


    public boolean isTokenValid(String token) {
        // TODO: Add more checks (e.g. expiration date)
        if (token != null && !token.isEmpty()) {
            Jws<Claims> claimsJws = null;

            try {
                claimsJws = Jwts.parser()
                        .setSigningKey("MyVeryCoolAndVerySecretivePasswordThatWillNeverBeFound".getBytes())
                        .build().parseSignedClaims(token);
            } catch (Exception e) {
                return false;
            }

        }

        return true;
    }

    public boolean isAdmin(String token) {
        // get subject from token
        if (token != null && !token.isEmpty()) {
            Jws<Claims> claimsJws = null;

            try {
                claimsJws = Jwts.parser()
                        .setSigningKey("MyVeryCoolAndVerySecretivePasswordThatWillNeverBeFound".getBytes())
                        .build().parseSignedClaims(token);
            } catch (Exception e) {
                return false;
            }

            String id = claimsJws.getBody().getSubject();
            // Find the user in the database
            Optional<User> user = userRepository.findById(Integer.valueOf(id));
            if (user.isPresent()) {
                return user.get().getIsAdmin();
            }
            else return false;
        }

        return false;
    }

}


