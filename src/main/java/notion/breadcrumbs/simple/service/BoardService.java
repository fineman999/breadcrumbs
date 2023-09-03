package notion.breadcrumbs.simple.service;

import lombok.RequiredArgsConstructor;
import notion.breadcrumbs.simple.domain.*;
import notion.breadcrumbs.simple.repsoitory.BoardRepository;

import java.util.List;

@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardPage findPageInfoById(Long id) {
        Board board = boardRepository.findById(id);
        checkNull(id, board);
        List<BoardSummary> parentSummary = boardRepository.findParentPagesById(id);
        List<BoardSummary> childIdsById = boardRepository.findSubPagesById(id);
        List<Title> titles = Title.reverseOf(parentSummary);
        Breadcrumbs breadcrumbs = Breadcrumbs.of(titles);
        return BoardPage.of(board, breadcrumbs, childIdsById);
    }

    private static void checkNull(Long id, Board board) {
        if (board == null) {
            throw new IllegalArgumentException("해당 게시글이 없습니다. id = " + id);
        }
    }
}
