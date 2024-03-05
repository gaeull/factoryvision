package webproject.factoryvision.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import webproject.factoryvision.domain.user.dto.UpdateUserDto;
import webproject.factoryvision.global.entity.BaseEntity;

import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public void setPassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    public void update(UpdateUserDto request, PasswordEncoder encoder) {
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            this.password = encoder.encode(request.getPassword());
        }
        if (request.getNickname() != null ) {
            this.nickname = request.getNickname();
        }
        if (request.getPhone() != null ) {
            this.phone = request.getPhone();
        }
        if (request.getEmail() != null ) {
            this.email = request.getEmail();
        }
        if (request.getName() != null ) {
            this.name = request.getName();
        }
        if (request.getUserId() != null ) {
            this.userId = request.getUserId();
        }
        if (request.getProfilePhoto() != null ) {
            this.profilePhoto = request.getProfilePhoto();
        }
    }

}
