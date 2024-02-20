package webproject.factoryvision.domain.user.service;

import lombok.Builder;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webproject.factoryvision.domain.user.dto.*;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.mapper.UserMapper;
import webproject.factoryvision.domain.user.repository.UserRepository;

import java.util.List;

@Service
@Builder
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
//    private final TokenProvider tokenProvider;
//    private final AuthenticationManager authenticationManager;


    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.toUserDtolist(userList);
    }

    @Transactional
    public void updateUser(long id, UpdateUserDto request) {
        User userInfo = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        userInfo.update(request);
    }

    public void signup(SignupRequest signupRequest) {
        User user = userMapper.toUser(signupRequest);

        // userId 중복 검사
        if (userRepository.existsByUserId(signupRequest.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        User savedUser = userRepository.save(user);
//        String token = tokenProvider.generateToken(savedUser, Duration.ofDays(14));
    }

//    public LoginResponse login(LoginRequest request) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));
//
//        User user = userRepository.findByUserId(request.getUserId());
//        String token = tokenProvider.generateToken(user, Duration.ofDays(14));
//        LoginResponse response = userMapper.toLoginResponse(user);
//        response.setToken(token);
//        return response;
//    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

}
