package notion.breadcrumbs.simple.domain;

import lombok.Builder;

@Builder
public record BoardSummary(Long id, String title) {

}
