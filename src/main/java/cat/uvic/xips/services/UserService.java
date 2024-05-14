package cat.uvic.xips.services;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.entities.Rating;
import cat.uvic.xips.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User save(UserCreationRequest userCreationRequest);

    User save(User user);

    okhttp3.Response createUserInOkta(UserCreationRequest userCreationRequest) throws IOException;

    List<User> findAll();


    void deleteUser(String username, UUID id);



    void setRating (Rating rating);

    User findUser(String username, UUID id);


}
