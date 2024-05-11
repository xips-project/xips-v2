package cat.uvic.xips.controller;

import cat.uvic.xips.dto.UpdateUserRequest;
import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.Rating;
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
import java.util.Optional;
import java.util.UUID;


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
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam(name = "username", required = false) String username,
                                     @RequestParam(name = "id", required = false) UUID id) {
        if (username != null) {
            return ResponseEntity.ok(userService.findByUsername(username));
        } else if (id != null) {
            return ResponseEntity.ok(userService.findUserById(id));
        } else {
            return ResponseEntity.badRequest().body("You must provide an id or username.");
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(name = "username", required = false) String username,
                                        @RequestParam(name = "id", required = false) UUID id) {
        if (username != null) {
            userService.deleteByUsername(username);
            return ResponseEntity.ok().body("User: "+username+" has been deleted");
        } else if (id != null) {
            userService.deleteById(id);
            return ResponseEntity.ok().body("User with id: "+id+" has been deleted");
        } else {
            return ResponseEntity.badRequest().body("You must provide an id or username.");
        }
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

    @PostMapping("/{username}/rating")
    private ResponseEntity<String> setRating(@PathVariable String username, @org.springframework.web.bind.annotation.RequestBody Rating ratingRequest){

        User user = userService.findByUsername(username);

        if (user==null){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Rating rating = Rating.builder()
                .user(user)
                .stars(ratingRequest.getStars())
                .message(ratingRequest.getMessage())
                .build();

        userService.setRating(rating);

        return ResponseEntity.ok().body(rating.toString());
    }

    @PostMapping("/{id}/rating")
    private ResponseEntity<String> setRatingById(@PathVariable UUID id, @RequestBody Rating ratingRequest){
        Optional<User> optionalUser = userService.findUserById(id);

        if (optionalUser.isPresent()){

            User user = optionalUser.get();

            Rating rating = Rating.builder()
                    .user(user)
                    .stars(ratingRequest.getStars())
                    .message(ratingRequest.getMessage())
                    .build();

            userService.setRating(rating);

            return ResponseEntity.ok().body(rating.toString());

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

    }

}
