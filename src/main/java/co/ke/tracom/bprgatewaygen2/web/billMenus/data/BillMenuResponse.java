package co.ke.tracom.bprgatewaygen2.web.billMenus.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillMenuResponse {
  private List<Menu> billMenus;
}
