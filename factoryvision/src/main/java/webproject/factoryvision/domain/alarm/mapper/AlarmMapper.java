package webproject.factoryvision.domain.alarm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import webproject.factoryvision.domain.alarm.dto.AlarmDto;
import webproject.factoryvision.domain.alarm.entity.Alarm;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlarmMapper {
    AlarmDto toAlarmDto(Alarm alarm);
    List<AlarmDto> toAlarmDtoList(List<Alarm> alarmList);
}
