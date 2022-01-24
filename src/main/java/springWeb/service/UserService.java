package springWeb.service;

import springWeb.DTO.request.UserRequest;
import springWeb.DTO.response.UserResponse;
import springWeb.repositoryJPA.entity.User;
import springWeb.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springWeb.repositoryJPA.UserRepositoryJPA;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepositoryJPA userRepositoryJPA;

    public List<UserResponse> findAll(){
        return userRepositoryJPA.findAll().stream().map(user -> new UserResponse().toReponse(user)).collect(Collectors.toList());
    }

    public UserResponse findById(Integer id) throws Exception{
        Optional<User> user = userRepositoryJPA.findById(id);

        if(user.isPresent()){
            return new UserResponse().toReponse(user.get());
        }else{
            throw new DomainException("Id: " + id + " not found in database");
        }
    }

    public User create (UserRequest userRequest){
        User userJpa = userRequest.toModel(userRequest);
        return userRepositoryJPA.save(userJpa);
    }

    public void delete (Integer id){
        userRepositoryJPA.deleteById(id);
    }

    public User update (Integer id, UserRequest userRequest){
        User entity = userRepositoryJPA.getById(id);//nao vai no banco de dados, só deixa um objeto monitorado pelo jpa para trabalhar com ele e depois efetuar a operação no bd
        updateDate(entity, userRequest);
        return userRepositoryJPA.save(entity);
    }

    private void updateDate(User entity, UserRequest userRequest) {
        entity.setName(userRequest.getName());
        entity.setEmail(userRequest.getEmail());
        entity.setPhone(userRequest.getPhone());
    }
}
