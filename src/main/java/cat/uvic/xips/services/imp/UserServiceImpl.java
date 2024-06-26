package cat.uvic.xips.services.imp;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.Rating;
import cat.uvic.xips.entities.User;
import cat.uvic.xips.entities.UserProfile;
import cat.uvic.xips.repositories.UserRepository;
import cat.uvic.xips.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found: ";
    public static final String CONTENT_TYPE = "application/json";
    private final UserRepository userRepository;
    @Setter
    private OkHttpClient client = new OkHttpClient();


    @Override
    public User save(UserCreationRequest userCreationRequest) {

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

        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @SneakyThrows
    public okhttp3.Response createUserInOkta(UserCreationRequest userCreationRequest) {
        MediaType mediaType = MediaType.parse(CONTENT_TYPE);
        String jsonBody = "{\"profile\": {\"firstName\": \"" + userCreationRequest.getFirstName()
                + "\",\"lastName\": \"" + userCreationRequest.getLastName()
                + "\",\"email\": \"" + userCreationRequest.getEmail()
                + "\",\"login\": \"" + userCreationRequest.getEmail()
                + "\"},\"credentials\": {\"password\": {\"value\": \"" + userCreationRequest.getPassword() + "\"}}}";

        RequestBody body = RequestBody.Companion.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url("https://dev-82475405.okta.com/api/v1/users?activate=false")
                .method("POST", body)
                .addHeader("Accept", CONTENT_TYPE)
                .addHeader("Content-Type", CONTENT_TYPE)
                .addHeader("Authorization", "SSWS 00E7XvYk01A3oILfuVfkyl_XmqhfA1JCtmbnLJfX3r")
                .build();


        return client.newCall(request).execute();

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    public void deleteUser(String username, UUID id) {

        throwExceptionIfNullParams(username, id);

        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));
            userRepository.delete(user);
        }

        if (id != null) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + id));
            userRepository.delete(user);
        }
    }

    @Override
    public void setRating(Rating rating) {
        User user = rating.getUser();
        user.getRatings().add(rating);
        userRepository.save(rating.getUser());
    }


    public User findUser(String username, UUID id) {

        throwExceptionIfNullParams(username, id);

        if (username != null) {
            return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));
        }

        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + id));

    }

    private void throwExceptionIfNullParams(String username, UUID id) {
        if (username == null && id == null) {
            throw new IllegalArgumentException("User not found");
        }
    }


}
