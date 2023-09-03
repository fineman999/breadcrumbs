package notion.breadcrumbs.simple.domain;

import java.util.List;
import java.util.StringJoiner;

public class Breadcrumbs {
    Breadcrumbs next;
    Title title;

    public Breadcrumbs() {
    }

    public Breadcrumbs(Title title) {
        this.title = title;
    }

    public static Breadcrumbs of(List<Title> titles) {
        Breadcrumbs dummy = new Breadcrumbs();
        Breadcrumbs node = dummy;
        for (Title title : titles) {
            node.next = new Breadcrumbs(title);
            node = node.next;
        }
        return dummy.next;
    }

    /**
     * 문자열 표현을 반환한다.
     * next가 null이면 title만 반환하고, 아니면 title /로 반환한다.
     */
    @Override
    public String toString() {
        if (next == null) return String.valueOf(title.title());
        return new StringJoiner(" / ", "", "")
                .add(String.valueOf(title.title()))
                .add(next.toString())
                .toString();
    }
}
