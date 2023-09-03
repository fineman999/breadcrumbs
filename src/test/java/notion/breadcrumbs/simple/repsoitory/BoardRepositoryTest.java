package notion.breadcrumbs.simple.repsoitory;

import lombok.RequiredArgsConstructor;
import notion.breadcrumbs.simple.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @TestConfiguration
    @RequiredArgsConstructor
    static class TestConfig {

        private final DataSource dataSource;
        @Bean
        BoardRepository boardRepository() {
            return new BoardRepositoryImpl(dataSource);
        }
    }


    @Test
    @DisplayName("루트 게시글 저장")
    void save() {
        Board board = Board.builder()
                .id(10L)
                .title("10 번째 루트 게시글")
                .content("10 번째 루트 게시글 내용")
                .createdAt(LocalDateTime.now())
                .build();
        int saved = boardRepository.save(board);

        assertThat(saved).isEqualTo(1);
    }

    @Test
    @DisplayName("서브 게시글 생성")
    void saveSub() {
        Board ParentBoard = Board.builder()
                .id(10L)
                .title("10 번째 루트 게시글")
                .content("10 번째 루트 게시글 내용")
                .createdAt(LocalDateTime.now())
                .build();
        boardRepository.save(ParentBoard);

        Board board = Board.builder()
                .id(20L)
                .title("20 서브 게시글")
                .content("20 서브 게시글 내용")
                .createdAt(LocalDateTime.now())
                .parentId(10L)
                .build();
        int saved = boardRepository.save(board);
        assertThat(saved).isEqualTo(1);
    }
}