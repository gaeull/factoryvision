package webproject.factoryvision.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import webproject.factoryvision.domain.user.dto.UpdateUserDto;
import webproject.factoryvision.global.entity.BaseEntity;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String name;

    @Column(name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profilePhoto;

    public void update(UpdateUserDto request) {
        this.password = request.getPassword();
        this.nickname = request.getNickname();
        this.phone = request.getPhone();
        this.email = request.getEmail();
        this.name = request.getName();
        this.userId = request.getUserId();
        this.profilePhoto = request.getProfilePhoto();
    }

}
