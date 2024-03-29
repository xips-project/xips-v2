package cat.uvic.xips.services;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.dto.UserDTO;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.entities.UserProfile;
import cat.uvic.xips.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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
    public okhttp3.Response createUserInOkta(UserCreationRequest userCreationRequest) throws IOException {
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

        return client.newCall(request).execute();
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
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: "+username));
        userRepository.delete(user);
    }
}
