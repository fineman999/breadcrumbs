package notion.breadcrumbs.simple.domain;


import java.util.Collections;
import java.util.List;

public record Title(String title) {

    public static List<Title> reverseOf(List<BoardSummary> parentSummary) {
        Collections.reverse(parentSummary);
        return parentSummary.stream()
                .filter(summary -> summary.title() != null)
                .map(summary -> new Title(summary.title()))
                .toList();
    }
}
