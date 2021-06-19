package co.ke.tracom.bprgateway.web.agenttransactions.services;

import co.ke.tracom.bprgateway.core.tracomchannels.json.RestHTTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalAgentValidation {

    private final RestHTTPService restHTTPService;

}
