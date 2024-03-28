package webproject.factoryvision.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import webproject.factoryvision.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class savedAlarmDto {
    private Long userId;
}
