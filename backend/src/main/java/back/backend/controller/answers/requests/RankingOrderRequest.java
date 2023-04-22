package back.backend.controller.answers.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingOrderRequest {
    String label;
    Integer value;
    Boolean sorted;
    Integer rank;
}
