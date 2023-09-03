package notion.breadcrumbs.simple.repsoitory;

import lombok.RequiredArgsConstructor;
import notion.breadcrumbs.simple.domain.Board;
import notion.breadcrumbs.simple.domain.BoardSummary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Sql("/insertBoardTest.sql")
    @DisplayName("기본 게시글 조회")
    @Test
    void findById() {
        Board board = boardRepository.findById(10L);

        assertThat(board.getId()).isEqualTo(10L);
    }

    @Sql("/insertBoardTest.sql")
    @DisplayName("subPages 게시글 조회")
    @Test
    void findSubPagesById() {
        List<BoardSummary> subPages = boardRepository.findSubPagesById(10L);

        assertAll(
                () -> assertThat(subPages.size()).isEqualTo(2),
                () -> assertThat(subPages.get(0).id()).isEqualTo(20L),
                () -> assertThat(subPages.get(0).title()).isEqualTo("title2"),
                () -> assertThat(subPages.get(1).id()).isEqualTo(30L),
                () -> assertThat(subPages.get(1).title()).isEqualTo("title3")
        );
    }

    @Sql("/insertBoardTest.sql")
    @Test
    @DisplayName("게시글 id 30을 검색하면 자신을 포함하여 계층형 구조의 모든 부모 id를 반환한다.")
    void findParentIdsByIds() {
        List<BoardSummary> parentTitlesById = boardRepository.findParentPagesById(30L);

        Assertions.assertAll(
                () -> assertThat(parentTitlesById.size()).isEqualTo(2),
                () -> assertThat(parentTitlesById.get(0).id()).isEqualTo(30L),
                () -> assertThat(parentTitlesById.get(1).id()).isEqualTo(10L),
                () -> assertThat(parentTitlesById.get(0).title()).isEqualTo("title3"),
                () -> assertThat(parentTitlesById.get(1).title()).isEqualTo("title1")
        );
    }
}