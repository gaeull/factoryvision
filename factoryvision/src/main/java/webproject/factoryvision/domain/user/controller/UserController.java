package webproject.factoryvision.domain.user.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.user.dto.*;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.mapper.UserMapper;
import webproject.factoryvision.domain.user.repository.UserRepository;
import webproject.factoryvision.domain.user.service.UserDetailsImpl;
import webproject.factoryvision.domain.user.service.UserService;
import webproject.factoryvision.domain.token.dto.ReissueTokenRequest;
import webproject.factoryvision.domain.token.TokenProvider;
import webproject.factoryvision.domain.token.dto.TokenResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final HttpHeaders getHeaders;

    // 전체 사용자 정보 조회
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userInfo")
    @Operation(summary = "전체 사용자 정보 조회")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    // 사용자 id별 정보 조회
    @GetMapping("/userInfo/{id}")
    @Operation(summary = "특정 사용자 정보 조회(id로 구분)")
    public ResponseEntity<?> getUserInfoByUserId(@PathVariable Long id) {
        GetUserInfoResponse user = userService.getUserById(id);
        return user != null ? ResponseEntity.status(HttpStatus.OK).body(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당되는 사용자가 없습니다.");
    }

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "request body에 회원정보 입력, user로 회원가입할경우 role:USER로, 정보 입력할때 값에 공백안들어가게 주의하기!!")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(getHeaders)
                .body("회원가입 완료");
    }

    // 로그인, accesstoken, refreshtoken이 생성해서 헤더로 제공.
    // refreshtoken은 레디스에 저장. accesstoken 만료되면, refreshtoken이용해서 accesstoken재발급에 사용됨.
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인id, 비밀번호 입력, 로그인 성공시 생성되는 accessToken 헤더에 포함시켜서 다른 api 요청하기")
    public ResponseEntity<?> login(@RequestBody SignInRequest request, HttpServletResponse response) {
        try {
            SignInResponse user = userService.login(request);
            TokenResponse token = tokenProvider.createTokenByLogin(user.getUserId(), user.getRole());//atk, rtk 생성
            response.addHeader(tokenProvider.AUTHORIZATION_HEADER, token.getAccessToken());// 헤더에 에세스 토큰만 싣기
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(getHeaders)
                    .body("비밀번호가 일치하지 않습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(getHeaders)
                    .body("사용자 정보가 없습니다.");
        }
    }

    // 로그아웃
    // 현 accessToken은 재사용못하게 레디스 blacklist에 저장
    // 로그아웃 --> 레디스에 저장된 refreshToken 삭제
    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        String accessToken = tokenProvider.resolveToken(request);
        userService.logout(accessToken, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .headers(getHeaders)
                .body("로그아웃 완료");
    }

    // AccessToken  재발급
    // API호출할때마다 시큐리티필터를 통해 인증 인가를 받음. 이때 만료된 토큰인지 검증하고 만료시 만료된토큰임을 에러메세지로 보냄.
    // 그럼 클라이언트에서 에러메세지를 확인 후, 이 api(atk 재발급)를 요청함.
    @PostMapping("/reissue-token")
    @Operation(summary = "accessToken 재발급", description = "API호출할때마다 시큐리티필터를 통해 인증 인가를 받음. 이때 만료된 토큰인지 검증하고 만료시 만료된토큰임을 에러메세지로 보냄, 그럼 클라이언트에서 에러메세지를 확인 후, 이 api(atk 재발급)를 요청함. 새로 생성한 accesstoken사용하기")
    public ResponseEntity<?> reissueToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody ReissueTokenRequest tokenRequest){
        try {
            //user 정보를 이용하여 토큰 발행
            SignUpResponse user = userMapper.toDto(userDetails.getUser());
            TokenResponse tokenResponse = tokenProvider.reissueAtk(user.getUserId(), user.getRole(), tokenRequest.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(getHeaders)
                    .body("유효하지 않은 refreshToken 입니다.");
        }
    }

    // 사용자 정보 수정
    @PostMapping("/{id}")
    @Operation(summary = "사용자 정보 수정(id로)", description = "id넣고 request body에 수정사항 반영하기")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody UpdateUserDto request) {
        try {
            userService.updateUser(id, request);
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(getHeaders)
                    .body("사용자 정보 수정 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(getHeaders)
                    .body("해당 사용자 정보 없음");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{id}")
    @Operation(summary = "회원 탈퇴(id로)", description = "id넣으면 사용자 정보 지워짐")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(getHeaders)
                .body("회원 탈퇴 완료");
    }

    // 토큰에서 유저 정보 가져오기
    @GetMapping("/tokenInfo")
    @Operation(summary = "토큰에서 유저 정보 가져오기", description = "request 헤더에서 토큰 추출해서 사용자id 출력")
    public ResponseEntity<?> getUserIdFromToken(HttpServletRequest request) {
        try {
            Long userIdFromToken = userService.getUserIdFromToken(request);
            return ResponseEntity.status(HttpStatus.OK).body(userIdFromToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(getHeaders)
                    .body("토큰이 유효하지 않습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(getHeaders)
                    .body("유저 정보가 없습니다.");
        }
    }

}
