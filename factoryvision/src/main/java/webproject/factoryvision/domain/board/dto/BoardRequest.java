package webproject.factoryvision.domain.board.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardRequest {
    private Long userId;
    private String title;
    private String content;
}
