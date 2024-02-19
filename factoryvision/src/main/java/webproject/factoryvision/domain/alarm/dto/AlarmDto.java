package webproject.factoryvision.domain.alarm.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import webproject.factoryvision.domain.user.entity.User;

@Getter
@Builder
public class AlarmDto {
    private Long id;
    private String userInfo;
    private String name;
    private String phone;
}
