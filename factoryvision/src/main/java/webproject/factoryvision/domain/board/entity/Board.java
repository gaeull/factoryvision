package webproject.factoryvision.domain.board.entity;

import jakarta.persistence.*;
import lombok.*;
import webproject.factoryvision.domain.board.dto.BoardRequest;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.repository.UserRepository;
import webproject.factoryvision.global.entity.BaseEntity;

@Entity
@Getter
@Setter
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

    public void update(BoardRequest request, UserRepository userRepository) {
        this.user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("id에 해당하는 유저 정보가 없습니다. : " + request.getUserId()));
        this.title = request.getTitle();
        this.content = request.getContent();
    }

}
