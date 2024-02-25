package webproject.factoryvision.domain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import webproject.factoryvision.domain.user.dto.*;
import webproject.factoryvision.domain.user.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toEntity(SignUpRequest request);
    SignUpResponse toDto(User user);
    List<UserDto> toUserDtolist(List<User> userList);
    SignInResponse toLoginResponse(User user);
    GetUserInfoResponse toUserInfo(User user);
}
