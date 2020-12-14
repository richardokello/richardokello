package co.ke.tracom.bprgatewaygen2.web.billMenus.service;


import co.ke.tracom.bprgatewaygen2.web.billMenus.data.BillMenuResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillMenusService {

  BillMenuResponse billMenuResponse;


  private final ResourceLoader resourceLoader;

  /**
   * This method gets run when this bean is initialized. It loads the data (json formatted)
   * about all bill Menus currently available.
   *
   * Bill menu data is saved a json file in the resources/data folder. Any new menu implemented in
   * this gateway should have its data saved in this json file.
   *
   * @throws IOException
   */
  @PostConstruct
  public void init() throws IOException {
    try {
      // Load all menus from a json file
      Resource resource = resourceLoader.getResource("classpath:data/billMenus.json");
      File file = resource.getFile();
      // Read file content
      String content = new String(Files.readAllBytes(file.toPath()));
      // Parse json file into menu items
      ObjectMapper mapper = new ObjectMapper();
      billMenuResponse = mapper.readValue(content, BillMenuResponse.class);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }

  public BillMenuResponse getAllMenus() {
    return billMenuResponse;
  }

}
