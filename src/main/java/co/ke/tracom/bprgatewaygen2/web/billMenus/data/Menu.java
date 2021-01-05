package co.ke.tracom.bprgatewaygen2.web.billMenus.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
  /* Name of bill Menu*/
  private String name;
  private boolean hasSubMenu;
  /* List of all sub menu items - empty if menu does not have submenus
   * A sub menu is a menu */
  private List<Menu> submenu;
  /* List of fields to be presented to the user for gathering their input.
   * Empty if submenu is present */
  private List<MenuField> fields;
}
