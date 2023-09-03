package notion.breadcrumbs.simple.repsoitory;

import notion.breadcrumbs.simple.domain.Board;
import notion.breadcrumbs.simple.domain.BoardSummary;

import java.util.List;

public interface BoardRepository {
    int save(Board board);
    Board findById(Long id);
    List<BoardSummary> findSubPagesById(Long id);
    List<BoardSummary> findParentPagesById(Long id);
}
