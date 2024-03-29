package cat.uvic.xips.controller;

import cat.uvic.xips.dto.UpdateUserRequest;
import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.dto.UserDTO;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.entities.UserProfile;
import cat.uvic.xips.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreationRequest userCreationRequest) throws IOException {
        userService.createUserInOkta(userCreationRequest);
        userService.save(userCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{username}")
    public ResponseEntity<?> getOne(@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/delete/{username}")
    public void deleteByUsername(@PathVariable String username){
        userService.deleteByUsername(username);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/update-user-data/{username}")
    public void updateUserData(@PathVariable String username, @Valid @RequestBody UpdateUserRequest request){
        User user = userService.findByUsername(username);
        user.setUserProfile(UserProfile.builder()
                        .birthdate(request.getBirthdate())
                        .address(request.getAddress())
                        .zipCode(request.getZipCode())
                        .country(request.getCountry())
                        .cityName(request.getCityName())
                .build());
    }

}
