package webproject.factoryvision.domain.user.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webproject.factoryvision.domain.user.dto.*;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.mapper.UserMapper;
import webproject.factoryvision.domain.user.repository.UserRepository;
import webproject.factoryvision.security.TokenProvider;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Builder
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;


    // userId별 사용자 정보 get
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 사용자 id별 정보 조회
    public GetUserInfoResponse getUserById(Long id) {
        return userMapper.toUserInfo(userRepository.findById(id).orElse(null));
    }

    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.toUserDtolist(userList);
    }

    @Transactional
    public void updateUser(Long id, UpdateUserDto request) {
        User userInfo = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        userInfo.update(request, encoder);
    }

    // 회원가입
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        User user = userMapper.toEntity(request);

        // userId 중복 검사
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        user.setPassword(encoder);

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    // 로그인
    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        User user = userRepository.findByUserId(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성
        String token = tokenProvider.createToken(String.format("%s", user.getId()));
        return new SignInResponse(user.getName(), token);
    }

    // 로그인 - 필터 사용
//    @Transactional(readOnly = true)
//    public SignInResponse signIn(SignInRequest request) {
//        User user = userRepository.findByUserId(request.getUserId())
//                .filter(it -> it.getPassword().equals(request.getPassword()))
//                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
//        return new SignInResponse(user.getName());
//    }

//    public User findById(Long userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
//    }

    // 회원 탈퇴
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
