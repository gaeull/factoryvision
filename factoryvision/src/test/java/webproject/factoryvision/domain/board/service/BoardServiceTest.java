package webproject.factoryvision.domain.board.service;

import jakarta.persistence.Access;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import webproject.factoryvision.domain.board.controller.BoardController;
import webproject.factoryvision.domain.board.dto.BoardRequest;
import webproject.factoryvision.domain.board.repository.BoardRepository;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.repository.UserRepository;
import webproject.factoryvision.domain.user.service.UserDetailsImpl;
import webproject.factoryvision.domain.user.service.UserService;
import webproject.factoryvision.token.TokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardController boardController;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private final long boardId = 1L;

    @BeforeEach
    void clean() {
        boardRepository.deleteAll();
    }

    @Test
    void updateBoard() {
        //given
        String userAToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoIjoiVVNFUiIsInN1YiI6InRlc3QxIiwiaWF0IjoxNzEwODQzNzU5LCJleHAiOjE3MTA4NzM3NTl9.Kd0Gtdosg17y2G5VG8Id3fkKdh_800TKh8SAi1vIFPE";
        String userBToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoIjoiVVNFUiIsInN1YiI6InRlc3QyIiwiaWF0IjoxNzEwODQzNzk1LCJleHAiOjE3MTA4NzM3OTV9.K6yEElAs5_YkQPqVGTW9OzxbRI7fdUyqa_ha9-AiyGU";

        BoardRequest request = new BoardRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

//        UserDetailsImpl userDetailsA = new UserDetailsImpl(new User("userA"));
//        UserDetailsImpl userDetailsB = new UserDetailsImpl(new User("userB"));

        // 토큰 프로바이더에서 사용자 토큰이 유효한지 확인하는 로직을 설정합니다.
        when(tokenProvider.validateToken(userAToken)).thenReturn(true);
        when(tokenProvider.validateToken(userBToken)).thenReturn(true);

        // When (실행)
        // 사용자 A로 게시글을 수정하는 경우
        when(tokenProvider.resolveToken(any())).thenReturn(userAToken);
        ResponseEntity<Void> responseA = boardController.updateBoard(boardId, request);

        // 사용자 B로 게시글을 수정하는 경우
        when(tokenProvider.resolveToken(any())).thenReturn(userBToken);
        ResponseEntity<Void> responseB = boardController.updateBoard(boardId, request);

        // Then (결과 확인)
        // 사용자 A는 수정에 성공해야 하고, 사용자 B는 수정에 실패해야 합니다.
        verify(boardController, times(1)).updateBoard(boardId, request); // updateBoard 메서드가 호출되어야 함

        // 사용자 A의 응답은 성공이어야 하고, 사용자 B의 응답은 실패여야 합니다.
        assert(responseA.getStatusCode().equals(HttpStatus.NO_CONTENT));
        assert(responseB.getStatusCode().equals(HttpStatus.FORBIDDEN));
    }
}