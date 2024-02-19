package webproject.factoryvision.domain.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webproject.factoryvision.domain.board.dto.BoardRequest;
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
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;
    private int views;

    public void update(BoardRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

}
