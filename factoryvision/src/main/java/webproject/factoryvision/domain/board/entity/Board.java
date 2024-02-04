package webproject.factoryvision.domain.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.global.entity.BaseEntity;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User userId;

    private String title;
    private String content;
    private int views;

}
