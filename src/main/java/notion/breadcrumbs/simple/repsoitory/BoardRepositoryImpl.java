package notion.breadcrumbs.simple.repsoitory;

import lombok.extern.slf4j.Slf4j;
import notion.breadcrumbs.simple.domain.Board;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
public class BoardRepositoryImpl implements BoardRepository{

    private final JdbcTemplate jdbcTemplate;

    public BoardRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int save(Board board) {
        String sql = "insert into boards(id, title, content, created_at, parent_id) values (?, ?, ?, ?, ?)";
        int update = jdbcTemplate.update(sql, board.getId(), board.getTitle(),
                board.getContent(), board.getCreatedAt(), board.getParentId());
        log.info("update = {}", update);
        return update;
    }

}
