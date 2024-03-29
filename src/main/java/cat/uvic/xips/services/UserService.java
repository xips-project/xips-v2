package cat.uvic.xips.services;

import cat.uvic.xips.dto.UserCreationRequest;
import cat.uvic.xips.dto.UserDTO;
import cat.uvic.xips.entities.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public void save(UserCreationRequest userCreationRequest);

    okhttp3.Response createUserInOkta(UserCreationRequest userCreationRequest) throws IOException;

    public List<User> findAll();

    public User findByUsername(String username);

    public void deleteByUsername(String username);

}
