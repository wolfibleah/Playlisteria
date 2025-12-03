package com.echipaMisterelor.playlisteriaAPI.Controller;

import com.echipaMisterelor.playlisteriaAPI.Exceptions.InvalidInputException;
import com.echipaMisterelor.playlisteriaAPI.Exceptions.ResourceNotFoundException;
import com.echipaMisterelor.playlisteriaAPI.Model.LoginResponse;
import com.echipaMisterelor.playlisteriaAPI.View.UserCredentials;
import com.echipaMisterelor.playlisteriaAPI.Model.User;
import com.echipaMisterelor.playlisteriaAPI.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(("/users"))
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    // Endpoints not directly accessible to all users

    @CrossOrigin
    @GetMapping("/{idUser}")
    public ResponseEntity<User> getUserByIdUser(@RequestHeader String authToken,
                                                @PathVariable Integer idUser) {
        if(!userService.isTokenValid(authToken) ||
            userService.getUserByIdUser(idUser).isEmpty() ||
                (!userService.isAdmin(authToken) && !userService.getIdFromToken(authToken).equals(idUser)))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Optional<User> _user = userService.getUserByIdUser(idUser);

        return _user.isEmpty() ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(_user.get(), HttpStatus.OK);
    }

    @PutMapping("/{oldIdUser}")
    public ResponseEntity<User> updateUser(@RequestHeader String authToken,
                                           @RequestBody User user, @PathVariable Integer oldIdUser) {
        if(!userService.isTokenValid(authToken) ||
                userService.getUserByIdUser(oldIdUser).isEmpty() ||
                (!userService.isAdmin(authToken) && !userService.getIdFromToken(authToken).equals(oldIdUser)))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User _user;
        try {
            _user = userService.updateUser(user, oldIdUser);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(_user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader String authToken){
        if(!userService.isTokenValid(authToken) || !userService.isAdmin(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader String authToken,
                                              @PathVariable Integer idUser) {
        if(!userService.isTokenValid(authToken) ||
                userService.getUserByIdUser(idUser).isEmpty() ||
                (!userService.isAdmin(authToken) && !userService.getIdFromToken(authToken).equals(idUser)))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(userService.deleteUser(idUser), userService.deleteUser(idUser) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    // Endpoints accessible to the user

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody User user) {
        LoginResponse registrationResponse = userService.registerUser(user);
        return new ResponseEntity<>(registrationResponse, registrationResponse.getMessage().equals("Success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserCredentials userCredentials){
        LoginResponse loginResponse = userService.loginUser(userCredentials);
        return new ResponseEntity<>(loginResponse, loginResponse.getMessage().equals("Success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
