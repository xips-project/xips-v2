package cat.uvic.xips.services;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.dto.UserDTO;
import cat.uvic.xips.entities.Rating;
import cat.uvic.xips.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void save(UserCreationRequest userCreationRequest);

    okhttp3.Response createUserInOkta(UserCreationRequest userCreationRequest) throws IOException;

    List<User> findAll();

    User findByUsername(String username);

    Optional<User> findUserById(UUID id);

    void deleteByUsername(String username);

    void deleteById(UUID id);

    void setRating (Rating rating);


}
