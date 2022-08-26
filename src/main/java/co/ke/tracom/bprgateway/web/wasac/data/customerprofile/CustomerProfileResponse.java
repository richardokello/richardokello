package co.ke.tracom.bprgateway.web.wasac.data.customerprofile;

import co.ke.tracom.bprgateway.web.academicbridge.data.studentdetails.GetStudentDetailsResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"response"})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileResponse {

    private String status;

    private String message;

   /* @JsonProperty("response")
    private Response data;*/
    private GetStudentDetailsResponse data;

    @Override
    public String toString() {
        return "CustomerProfileResponse{" + "status='" + status + '\'' + ", response=" + data + '}';
    }
}
