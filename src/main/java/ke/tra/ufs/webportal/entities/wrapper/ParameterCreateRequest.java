package ke.tra.ufs.webportal.entities.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterCreateRequest<T> {
    private Object category;
    private Set<T> indices;
}
