package webproject.factoryvision.domain.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webproject.factoryvision.config.SecurityConfig;
import webproject.factoryvision.domain.user.dto.*;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.mapper.UserMapper;
import webproject.factoryvision.domain.user.repository.UserRepository;
import webproject.factoryvision.redis.CacheNames;
import webproject.factoryvision.redis.RedisDao;
import webproject.factoryvision.domain.token.TokenProvider;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Builder
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TokenProvider tokenProvider;
    private final RedisDao redisDao;
    private final SecurityConfig securityConfig;

//    public Optional<User> getUserByUserId(String userId) {
//        return userRepository.findByUserId(userId);
//    }

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
        userInfo.update(request, securityConfig.PasswordEncoder());
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        User user = userMapper.toEntity(request);

        // userId 중복 검사
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        user.setPassword(securityConfig.PasswordEncoder());
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Cacheable(cacheNames = CacheNames.LOGINUSER, key = "'login'+ #p0.getUserId()", unless = "#result == null")
    @Transactional
    public SignInResponse login(SignInRequest request) {
        User user = userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("사용자 정보가 없습니다."));
        if (!securityConfig.PasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return userMapper.toLoginResponse(user);
    }

    @CacheEvict(cacheNames = CacheNames.USERBYUSERID, key = "'login'+#p1")
    @Transactional
    public void logout(String accessToken, String userId) {
        // 레디스에 accessToken 사용못하도록 등록
        Long expiration = tokenProvider.getExpiration(accessToken);

        log.info("expiration 값: {}", expiration);

        redisDao.setBlackList(accessToken, "logout", expiration);

        if (redisDao.hasKey(userId)) {
            log.info("userId키가 있는 것 까지 확인");
            redisDao.deleteRefreshToken(userId);
            log.info("Refresh token for user {}", userId);
        } else {
            throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
        }
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
