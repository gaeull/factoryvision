package webproject.factoryvision.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import webproject.factoryvision.global.entity.BaseEntity;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

}
