package co.ke.tracom.bprgateway.web.exceptions.custom;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/** Enables all exceptions report same format of message */
@Component
public class CustomErrorDetails extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
    // Get all the normal error information
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

    // Linked HashMaps maintain the order the items are inserted. It's used here so that the error
    // JSON
    // produced from this class lists the attributes in the same order as other classes.
    Map<String, Object> errorDetails = new LinkedHashMap<>();
    errorDetails.put("title", errorAttributes.get("error"));
    errorDetails.put("status", errorAttributes.get("status"));
    errorDetails.put("message", errorAttributes.get("message"));
    errorDetails.put("timestamp", errorAttributes.get("timestamp"));

    return errorDetails;
  }
}
