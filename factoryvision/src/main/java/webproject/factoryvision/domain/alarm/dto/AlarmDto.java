package webproject.factoryvision.domain.alarm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class AlarmDto {
    private Long id;
    private String userInfo;
    private String name;
    private String phone;
    private String createdAt;

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
