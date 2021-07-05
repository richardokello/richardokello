package co.ke.tracom.bprgateway.web.switchparameters;

import co.ke.tracom.bprgateway.web.exceptions.custom.XSwitchParameterException;
import co.ke.tracom.bprgateway.web.switchparameters.entities.XSwitchParameter;
import co.ke.tracom.bprgateway.web.switchparameters.repository.XSwitchParameterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class XSwitchParameterService {
    private final XSwitchParameterRepository xSwitchParameterRepository;

    public String fetchXSwitchParamValue(String parameter){
        if(parameter == null){
            log.info("Invalid switch parameter request.");
            throw new XSwitchParameterException("Invalid parameter request. Please contact BPR Customer care");
        }

        Optional<XSwitchParameter> optionalXSwitchParameter = xSwitchParameterRepository.findByParamName(parameter);
        if(optionalXSwitchParameter.isEmpty()){
            log.info("Missing XSwitch parameter configuration ["+parameter+"].");
            throw new XSwitchParameterException("Missing parameter configuration ["+parameter+"]. Please contact BPR Customer Care");
        }
        return optionalXSwitchParameter.get().getParamValue();
    }
}
