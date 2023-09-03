package notion.breadcrumbs.simple.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.StringJoiner;

@Getter
public class BoardPage {
    private final Long id;
    private final String title;
    private final String content;

    private final Breadcrumbs breadcrumbs;

    private final List<BoardSummary> subPages;

    @Builder
    public BoardPage(Long id, String title, String content, Breadcrumbs breadcrumbs, List<BoardSummary> subPages) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.breadcrumbs = breadcrumbs;
        this.subPages = subPages;
    }


    public static BoardPage of(Board board, Breadcrumbs breadcrumbs, List<BoardSummary> childIdsById) {
        return BoardPage.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .breadcrumbs(breadcrumbs)
                .subPages(childIdsById)
                .build();
    }

    @Override
    public String toString() {
        return new StringJoiner(",\n   ", "{ \n \"" + BoardPage.class.getSimpleName() + "\" : { \n   ", "}")
                .add("\"id\": " + id )
                .add("\"title\": \"" + title + "\"")
                .add("\"content\": \"" + content + "\"")
                .add("\"subPages\": " + subPages)
                .add("\"breadcrumbs\": \"" + breadcrumbs + "\"")
                .add("\n")
                .toString();
    }
}
