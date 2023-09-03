package notion.breadcrumbs.simple.service;

import lombok.RequiredArgsConstructor;
import notion.breadcrumbs.simple.domain.BoardPage;
import notion.breadcrumbs.simple.repsoitory.BoardRepository;
import notion.breadcrumbs.simple.repsoitory.BoardRepositoryImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
@Transactional
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @TestConfiguration
    @RequiredArgsConstructor
    static class TestConfig {

        private final DataSource dataSource;
        @Bean
        BoardRepository boardRepository() {
            return new BoardRepositoryImpl(dataSource);
        }
        @Bean
        BoardService boardService() {
            return new BoardService(boardRepository());
        }
    }

    @Sql("/insertBoardTest.sql")
    @Test
    @DisplayName("게시글 조회")
    void get()  {
        BoardPage pageInfoById = boardService.findPageInfoById(30L);

        Assertions.assertAll(
                () -> assertThat(pageInfoById.getId()).isEqualTo(30L),
                () -> assertThat(pageInfoById.getTitle()).isEqualTo("title3"),
                () -> assertThat(pageInfoById.getContent()).isEqualTo("content3"),
                () -> assertThat(pageInfoById.getSubPages().toString()).isEqualTo("[[\"id\": 70, \"title\": \"title7\"], [\"id\": 80, \"title\": \"title8\"]]"),
                () -> assertThat(pageInfoById.getBreadcrumbs().toString()).isEqualTo("title1 / title3")
        );
        System.out.println("pageInfoById = " + pageInfoById);
    }
}