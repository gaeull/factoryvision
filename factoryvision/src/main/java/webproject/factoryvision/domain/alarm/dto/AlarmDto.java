package webproject.factoryvision.domain.alarm.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmDto {
    private Long id;
    private String userInfo;
    private String name;
    private String phone;
}
