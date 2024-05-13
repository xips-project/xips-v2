package cat.uvic.xips.services;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.Rating;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.entities.UserProfile;
import cat.uvic.xips.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OkHttpClient client = new OkHttpClient();


    @Override
    public void save(UserCreationRequest userCreationRequest) {

        UserProfile userProfile = UserProfile.builder()
                .firstname(userCreationRequest.getFirstName())
                .lastname(userCreationRequest.getLastName())
                .birthdate(userCreationRequest.getBirthdate())
                .address(userCreationRequest.getAddress())
                .cityName(userCreationRequest.getCityName())
                .zipCode(userCreationRequest.getZipCode())
                .country(userCreationRequest.getCountry())
                .build();

        User user = User.builder()
                .username(userCreationRequest.getEmail())
                .email(userCreationRequest.getEmail())
                .role(userCreationRequest.getRole())
                .userProfile(userProfile)
                .build();

        userProfile.setUser(user);

        userRepository.save(user);
    }

    @Override
    @SneakyThrows
    public okhttp3.Response createUserInOkta(UserCreationRequest userCreationRequest) {
        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = "{\"profile\": {\"firstName\": \"" + userCreationRequest.getFirstName()
                + "\",\"lastName\": \"" + userCreationRequest.getLastName()
                + "\",\"email\": \"" + userCreationRequest.getEmail()
                + "\",\"login\": \"" + userCreationRequest.getEmail()
                + "\"},\"credentials\": {\"password\": {\"value\": \"" + userCreationRequest.getPassword() + "\"}}}";

        RequestBody body = RequestBody.Companion.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url("https://dev-82475405.okta.com/api/v1/users?activate=false")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "SSWS 00E7XvYk01A3oILfuVfkyl_XmqhfA1JCtmbnLJfX3r")
                .build();


        try (var okhttpResponse = client.newCall(request).execute()) {
            return okhttpResponse;
        } catch (IOException e) {
                throw new RuntimeException();
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }


    @Override
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        userRepository.delete(user);
    }

    @Override
    public void deleteById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void setRating(Rating rating) {
        User user = rating.getUser();
        user.getRatings().add(rating);
        userRepository.save(rating.getUser());
    }


}
