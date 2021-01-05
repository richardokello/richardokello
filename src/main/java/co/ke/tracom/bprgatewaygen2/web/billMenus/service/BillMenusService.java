package co.ke.tracom.bprgatewaygen2.web.billMenus.service;

import co.ke.tracom.bprgatewaygen2.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgatewaygen2.web.exceptions.custom.ExternalHTTPRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillMenusService {

  private final ResourceLoader resourceLoader;
  BillMenuResponse billMenuResponse;

  /**
   * This method gets run when this bean is initialized. It loads the data (json formatted) about
   * all bill Menus currently available.
   *
   * <p>Bill menu data is saved in a json file in the resources/data folder. Any new menu
   * implemented in this gateway should have its data added to this json file.
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
      log.info("BILL MENU SERVICE RESPONSE: {}", billMenuResponse);
    } catch (Exception ex) {
      ex.printStackTrace();
      log.error("BILL MENU SERVICE: {}", ex.getMessage());
      throw new ExternalHTTPRequestException("Error fetching bill menus");
    }
  }

  public BillMenuResponse getAllMenus() {
    return billMenuResponse;
  }
}
