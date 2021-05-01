package co.ke.tracom.bprgateway.web.switchparameters.repository;

import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface XSwitchParameterRepository extends CrudRepository<XSwitchParameter, Long> {
  Optional<XSwitchParameter> findByParamName(String parameterName);

}
