package notion.breadcrumbs.simple.repsoitory;

import notion.breadcrumbs.simple.domain.Board;

public interface BoardRepository {
    int save(Board board);
}
