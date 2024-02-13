package webproject.factoryvision.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private Long id;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String email;
    private String userId;
    private String profilePhoto;
}
