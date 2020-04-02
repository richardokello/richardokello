package ke.tra.com.tsync.wrappers.crdb;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionResponse {
    String sessionToken;
    String message;
    String requestID;
}
