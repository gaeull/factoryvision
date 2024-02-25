package webproject.factoryvision.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.user.dto.*;
//import webproject.factoryvision.domain.user.service.TokenProvider;
//import webproject.factoryvision.domain.user.service.TokenService;
import webproject.factoryvision.domain.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision")
public class UserController {

    private final UserService userService;

    // 전체 사용자 정보 조회
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userInfo")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // 사용자 id별 정보 조회
    @GetMapping("/userInfo/{id}")
    public GetUserInfoResponse getUserInfoByUserId(Long id) {
        return userService.getUserById(id);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest request) {
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest request) {
        SignInResponse response = userService.signIn(request);
        return ResponseEntity.ok(response);
    }

    // 로그아웃


    // 사용자 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id, @RequestBody UpdateUserDto request) {
        userService.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }


}
