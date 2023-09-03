package notion.breadcrumbs.simple.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
public class Board {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final Long parentId;

    @Builder
    public Board(Long id, String title, String content, LocalDateTime createdAt, Long parentId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Board.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .add("content='" + content + "'")
                .add("createdAt=" + createdAt)
                .add("parentId=" + parentId)
                .toString();
    }
}
