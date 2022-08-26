package co.ke.tracom.bprgateway.web.billMenus.service;

import co.ke.tracom.bprgateway.web.billMenus.data.BillMenuResponse;
import co.ke.tracom.bprgateway.core.config.CustomObjectMapper;
import co.ke.tracom.bprgateway.web.exceptions.custom.ExternalHTTPRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

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
   * <p>Bill menu data is saved in a billsMenus.json file in the resources/data folder. Any new menu
   * implemented in this gateway should have its data added to this json file.
   *
   * @throws IOException
   */
  @PostConstruct
  public void init() throws IOException {
    try {
      // Load all menus from a json file
      Resource resource = resourceLoader.getResource("classpath:data/billMenus.json");
      InputStream jsonAsStream = resource.getInputStream();
      // Parse json file into menu items
      CustomObjectMapper mapper = new CustomObjectMapper();
      billMenuResponse = mapper.readValue(jsonAsStream, BillMenuResponse.class);
    } catch (Exception ex) {
      log.error("BILL MENU SERVICE: {}", ex.getMessage());
      throw new ExternalHTTPRequestException("Error fetching bill menus");
    }
  }

  public BillMenuResponse fetchEnglishMenus() {
    return billMenuResponse;
  }
  public BillMenuResponse fetchKinyarwandaMenus() {
    return billMenuResponse;
  }
}
