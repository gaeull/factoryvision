package webproject.factoryvision.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.user.dto.*;
import webproject.factoryvision.domain.user.entity.User;
//import webproject.factoryvision.domain.user.service.TokenProvider;
//import webproject.factoryvision.domain.user.service.TokenService;
import webproject.factoryvision.domain.user.service.UserService;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("factoryvision")
public class UserController {

    private final UserService userService;
//    private final TokenService tokenService;
//    private final TokenProvider tokenProvider;

    @PostMapping()
    public String Test(){
        return "test";
    }

    // 전체 사용자 정보 조회
    @GetMapping("/userInfo")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
//        UserDto user = userService.signup(request);
        userService.signup(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 로그인
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
//        userService.login(request);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    // 사용자 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id, @RequestBody UpdateUserDto request) {
        userService.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }


}
