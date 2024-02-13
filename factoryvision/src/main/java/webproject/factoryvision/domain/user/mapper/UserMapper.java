package webproject.factoryvision.domain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import webproject.factoryvision.domain.user.dto.LoginResponse;
import webproject.factoryvision.domain.user.dto.SignupRequest;
import webproject.factoryvision.domain.user.dto.UserDto;
import webproject.factoryvision.domain.user.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(SignupRequest request);
    List<UserDto> toUserDtolist(List<User> userList);
    LoginResponse toLoginResponse(User user);
}
