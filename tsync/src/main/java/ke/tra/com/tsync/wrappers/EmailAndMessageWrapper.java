package ke.tra.com.tsync.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAndMessageWrapper {
    private String message;
    private String messageType;
    private String sendTo;
    private String subject;
}
