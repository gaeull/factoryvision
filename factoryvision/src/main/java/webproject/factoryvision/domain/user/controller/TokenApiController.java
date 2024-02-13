//package webproject.factoryvision.domain.user.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import webproject.factoryvision.domain.user.dto.CreateAccessTokenRequest;
//import webproject.factoryvision.domain.user.dto.CreateAccessTokenResponse;
//import webproject.factoryvision.domain.user.service.TokenService;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("factoryvision")
//public class TokenApiController {
//    private final TokenService tokenService;
//
//    @PostMapping("/api/token")
//    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
//        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new CreateAccessTokenResponse(newAccessToken));
//    }
//}
