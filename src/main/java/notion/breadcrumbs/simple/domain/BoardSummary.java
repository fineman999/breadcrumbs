package notion.breadcrumbs.simple.domain;

import lombok.Builder;

import java.util.StringJoiner;

@Builder
public record BoardSummary(Long id, String title) {


    /**
     * 문자열 표현을 반환한다.
     * ex) ["id": 1, "title": "title"] 형태로 반환한다.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", "[", "]")
                .add("\"id\": " + id)
                .add("\"title\": \"" + title + "\"")
                .toString();
    }
}
