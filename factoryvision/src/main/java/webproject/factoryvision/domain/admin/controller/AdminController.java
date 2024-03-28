package webproject.factoryvision.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import webproject.factoryvision.domain.admin.service.AdminService;
import webproject.factoryvision.domain.alarm.dto.AlarmDto;
import webproject.factoryvision.domain.user.dto.UserDto;

import java.util.List;

@Tag(name = "관리자용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/factoryvision/admin")
public class AdminController {

    private final AdminService adminService;

    // 전체 사용자 정보 조회
    @GetMapping("/userInfo")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "전체 사용자 정보 조회")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = adminService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    // 호출 알림 조회
    @GetMapping("/emergency")
    @Operation(summary = "알림 호출한 사용자 정보 조회", description = "알람id, 사용자id, 사용자이름, 사용자전화번호 출력")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AlarmDto>> getAllAlarms() {
        List<AlarmDto> allAlarms = adminService.getAllAlarms();
        return ResponseEntity.status(HttpStatus.OK).body(allAlarms);
    }

}
