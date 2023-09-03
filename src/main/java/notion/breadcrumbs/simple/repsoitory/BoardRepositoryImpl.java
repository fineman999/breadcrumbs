package notion.breadcrumbs.simple.repsoitory;

import lombok.extern.slf4j.Slf4j;
import notion.breadcrumbs.simple.domain.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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

    @Override
    public Board findById(Long id) {
        String sql = "select * from boards where id = ?";
        return jdbcTemplate.queryForObject(sql, boardRowMapper(), id);
    }


    private RowMapper<Board> boardRowMapper() {
        return (rs, rowNum) -> Board.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .content(rs.getString("content"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .parentId(rs.getLong("parent_id"))
                .build();
    }
}
