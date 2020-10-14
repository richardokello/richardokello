package ke.tra.ufs.webportal.resources;

import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tra.ufs.webportal.entities.UfsEdittedRecord;
import ke.tra.ufs.webportal.entities.UfsPosRole;
import ke.tra.ufs.webportal.repository.UfsPosRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;


@RestController
@RequestMapping("/pos-role")
public class UfsPosRoleController extends ChasisResource<UfsPosRole, UfsEdittedRecord,Long> {

    private final UfsPosRoleRepository posRoleRepository;

    public UfsPosRoleController(LoggerService loggerService, EntityManager entityManager,
                                UfsPosRoleRepository posRoleRepository) {
        super(loggerService, entityManager);
        this.posRoleRepository = posRoleRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsPosRole>> create(@Valid @RequestBody UfsPosRole ufsPosRole) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsPosRole posRole = posRoleRepository.findByPosRoleNameAndIntrash(ufsPosRole.getPosRoleName(), AppConstants.NO);

        if (posRole != null ) {
            responseWrapper.setCode(HttpStatus.CONFLICT.value());
            responseWrapper.setMessage(ufsPosRole.getPosRoleName()+" Role Name already exist");

            return new ResponseEntity(responseWrapper, HttpStatus.CONFLICT);
        }
        return super.create(ufsPosRole);
    }
}
