package webproject.factoryvision.domain.alarm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.global.entity.BaseEntity;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_phone")
    private String phone;

}
