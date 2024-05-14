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

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<User> create(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        userService.createUserInOkta(userCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreationRequest));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam(name = "username", required = false) String username,
                                        @RequestParam(name = "id", required = false) UUID id) {

        return ResponseEntity.ok(userService.findUser(username, id));
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam(name = "username", required = false) String username,
                                                 @RequestParam(name = "id", required = false) UUID id) {
        userService.deleteUser(username, id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/update-user-data/{username}")
    public void updateUserData(@PathVariable String username, @Valid @RequestBody UpdateUserRequest request) {
        User user = userService.findUser(username,null);
        user.setUserProfile(UserProfile.builder()
                .birthdate(request.getBirthdate())
                .address(request.getAddress())
                .zipCode(request.getZipCode())
                .country(request.getCountry())
                .cityName(request.getCityName())
                .build());

        userService.save(user);
    }

    @PostMapping("/{username}/rating")
    private ResponseEntity<Rating> setRating(@PathVariable String username, @org.springframework.web.bind.annotation.RequestBody Rating ratingRequest) {

        User user = userService.findUser(username,null);

        Rating rating = Rating.builder()
                .user(user)
                .stars(ratingRequest.getStars())
                .message(ratingRequest.getMessage())
                .build();

        userService.setRating(rating);

        return ResponseEntity.ok().body(rating);
    }

    @PostMapping("/{id}/rating")
    private ResponseEntity<Rating> setRatingById(@PathVariable UUID id, @RequestBody Rating ratingRequest) {
        User user = userService.findUser(null,id);

        Rating rating = Rating.builder()
                .user(user)
                .stars(ratingRequest.getStars())
                .message(ratingRequest.getMessage())
                .build();

        userService.setRating(rating);

        return ResponseEntity.ok().body(rating);

    }
}