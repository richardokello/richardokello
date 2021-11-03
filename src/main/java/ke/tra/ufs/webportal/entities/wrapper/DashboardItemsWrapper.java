package ke.tra.ufs.webportal.entities.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardItemsWrapper {
    private String name;
    private Long value;
    private String link;
}
