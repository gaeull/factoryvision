package webproject.factoryvision.domain.alarm.dto;

import lombok.Builder;
import lombok.Getter;
import webproject.factoryvision.domain.user.entity.User;

@Getter
@Builder
public class savedAlarmDto {
    private String userId;
    private String name;
    private String phone;
}
