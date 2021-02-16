/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.ErrorList;
import ke.axle.chassis.wrappers.ActionWrapper;
import ke.axle.chassis.wrappers.ResponseWrapper;
import ke.tracom.ufs.entities.*;
import ke.tracom.ufs.entities.wrapper.DeactivateAccountWrapper;
import ke.tracom.ufs.entities.wrapper.UserFilter;
import ke.tracom.ufs.repositories.*;
import ke.tracom.ufs.services.UserService;
import ke.tracom.ufs.services.UserTypesService;
import ke.tracom.ufs.services.WorkGroupService;
import ke.tracom.ufs.services.template.FileStorageService;
import ke.tracom.ufs.services.template.HttpCall;
import ke.tracom.ufs.services.template.LoggerServiceTemplate;
import ke.tracom.ufs.services.template.NotifyServiceTemplate;
import ke.tracom.ufs.utils.AppConstants;
import ke.tracom.ufs.utils.PasswordGenerator;
import ke.tracom.ufs.utils.exceptions.DataExistsException;
import ke.tracom.ufs.wrappers.LogExtras;
import ke.tracom.ufs.wrappers.OauthResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Cornelius M
 * @author Owori Juma
 * @author emuraya
 */
@Controller
@RequestMapping(value = "/user")
@CommonsLog
public class UserResource extends ChasisResource<UfsUser, Long, UfsEdittedRecord> {

    @Value("${client_id}")
    private String client_id;

    @Value("${client_secret}")
    private String client_secret;

    @Value("${server.port}")
    private String port;

    @Autowired
    private HttpCall<OauthResponse> oauthResponseHttpCall;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserService urepo;
    private final WorkGroupService workGroupService;
    private final PasswordGenerator gen;
    private final FileStorageService fileStorageService;
    private final UfsAuthTypeRepository authTypeRepo;
    private final UfsUserWorkgroupRepository userWrkgroupRepo;
    private final UserTypesService userTypesService;
    private final UfsUserTypeRepository userTypeRepository;
    private final UfsEdittedRecordRepository edittedRecordRepository;
    private final LogExtras logExtras;
    @Autowired
    UfsSysConfigRepository configRepo;
    private final AuthenticationRepository authRepository;
    private final NotifyServiceTemplate notifyService;
    private final UserRepository userRepository;

    String password;

    public UserResource(LoggerServiceTemplate loggerService, EntityManager entityManager, UserService urepo, WorkGroupService workGroupService, PasswordGenerator gen,
                        FileStorageService fileStorageService, UfsAuthTypeRepository authTypeRepo, UfsUserWorkgroupRepository userWrkgroupRepo, UserTypesService userTypesService,
                        UfsUserTypeRepository userTypeRepository, UfsEdittedRecordRepository edittedRecordRepository, AuthenticationRepository authRepository, NotifyServiceTemplate notifyService, LogExtras logExtras, UserRepository userRepository) {
        super(loggerService, entityManager);
        this.urepo = urepo;
        this.workGroupService = workGroupService;
        this.gen = gen;
        this.fileStorageService = fileStorageService;
        this.authTypeRepo = authTypeRepo;
        this.userWrkgroupRepo = userWrkgroupRepo;
        this.userTypesService = userTypesService;
        this.userTypeRepository = userTypeRepository;
        this.edittedRecordRepository = edittedRecordRepository;
        this.authRepository = authRepository;
        this.notifyService = notifyService;
        this.logExtras = logExtras;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ResponseWrapper<Page<UfsUser>>> findAll(Pageable pg, HttpServletRequest request) throws ParseException {
        ResponseWrapper response = new ResponseWrapper();
        if(request.getUserPrincipal() == null){
            response.setCode(401);
            response.setMessage("Unauthorized");
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        String username = request.getUserPrincipal().getName();
        UfsAuthentication auth = authRepository.findByusernameIgnoreCase(username);
        UfsUser user = auth.getUser();
        UserFilter filter = new UserFilter();
        if (request.getParameter("from") != null)
            filter.setFrom(this.tryParse(request.getParameter("from")));
        if (request.getParameter("needle") != null)
            filter.setNeedle(request.getParameter("needle"));
        if (request.getParameter("actionStatus") != null)
            filter.setActionStatus(request.getParameter("actionStatus"));
        if (request.getParameter("to") != null)
            filter.setTo(this.tryParse(request.getParameter("to")));

        //filter by status
        if (request.getParameter("status") != null) {
            filter.setStatus(Short.valueOf(request.getParameter("status")));
            response.setData(urepo.fetchUsersExcludes(user, filter.getActionStatus(), filter.getFrom(), filter.getTo(), filter.getNeedle(),filter.getStatus(), pg));
            return new ResponseEntity(response, HttpStatus.OK);
        }

        //filter by action
        if (request.getParameter("action") != null) {
            filter.setAction(request.getParameter("action"));
            response.setData(urepo.fetchUsersAction(user, filter.getActionStatus(), filter.getFrom(), filter.getTo(), filter.getNeedle(),filter.getAction(), pg));
            return new ResponseEntity(response, HttpStatus.OK);
        }

        //filter by userType
        if (request.getParameter("userType") != null) {
            filter.setUserType(new BigDecimal(request.getParameter("userType")));
            response.setData(urepo.fetchUsersUserType(user, filter.getActionStatus(), filter.getFrom(), filter.getTo(), filter.getNeedle(),filter.getUserType(), pg));
            return new ResponseEntity(response, HttpStatus.OK);
        }

        response.setData(urepo.fetchUsersExclude(user, filter.getActionStatus(), filter.getFrom(), filter.getTo(), filter.getNeedle(), pg));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/profile-upload")
    public ResponseEntity<ResponseWrapper> uploadFile(@RequestParam(name = "file") MultipartFile file, @RequestParam("userId") String userId) throws Exception {
        ResponseWrapper wrap = new ResponseWrapper();

        String fileName = fileStorageService.storeFile(file, (gen.generateRandomPassword() + "-" + userId + "-"));

        UfsUser usr = urepo.findByUserId(Long.parseLong(userId));
        usr.setAvatar(fileName);
        urepo.save(usr);

        wrap.setCode(201);
        wrap.setMessage("Upload Successfully");
        wrap.setData(fileName);
        return ResponseEntity.ok().body(wrap);

    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<UfsUser>> create(@Valid @RequestBody UfsUser ufsUser) {

        if (urepo.findByusernameIgnoreCase(ufsUser.getEmail()) != null) {
            try {
                throw new DataExistsException(ufsUser.getEmail() + " Already Exists");
            } catch (DataExistsException e) {
                e.printStackTrace();
            }
        }

        ufsUser.setStatus(AppConstants.STATUS_EXPIRED);

        ResponseEntity<ResponseWrapper<UfsUser>> response = super.create(ufsUser);

        password = gen.generateRandomPassword();


        UfsAuthentication ufsAuthentication = new UfsAuthentication();
        ufsAuthentication.setUserId(ufsUser.getUserId());
        ufsAuthentication.setUsername(ufsUser.getEmail());
        ufsAuthentication.setPassword(passwordEncoder.encode(password));
        ufsAuthentication.setPasswordStatus(AppConstants.STATUS_EXPIRED);
        ufsAuthentication.setAuthenticationTypeId(authTypeRepo.findByAuthenticationType(AppConstants.AUTH_TYPE_PASSWORD).getTypeId());
        urepo.saveAuthentication(ufsAuthentication);


        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            List<UfsUserWorkgroup> usrworkgroups = new ArrayList<>();
            ufsUser.getWorkgroupIds().stream().forEach(id -> {
                UfsUserWorkgroup usrworkgroup = new UfsUserWorkgroup();
                usrworkgroup.setUserId(ufsUser.getUserId());
                usrworkgroup.setGroupId(id);
                usrworkgroups.add(usrworkgroup);

            });
            workGroupService.saveAll(usrworkgroups);
        }

        UfsUserType userType = userTypeRepository.findBytypeId(ufsUser.getUserTypeId());
        switch (userType.getUserType()) {
            case AppConstants.USER_REGIONAL_MANAGER:
                UfsUserRegionMap regionMap = new UfsUserRegionMap();
                regionMap.setId(null);
                regionMap.setRegionIds(ufsUser.getRegionIds());
                regionMap.setUserIds(ufsUser.getUserId());
                userTypesService.saveRegionhead(regionMap);
                break;
            case AppConstants.USER_AGENT_SUPERVISOR:
                UfsUserAgentSupervisor agentSupervisor = new UfsUserAgentSupervisor();
                agentSupervisor.setId(null);
                agentSupervisor.setBranchIds(ufsUser.getBranchIds());
                agentSupervisor.setRegionIds(ufsUser.getRegionIds());
                agentSupervisor.setUserIds(ufsUser.getUserId());
                userTypesService.saveSupervisor(agentSupervisor);
                break;
            case AppConstants.USER_BRANCH_MANAGER:
                UfsUserBranchManagers branchManagers = new UfsUserBranchManagers();
                branchManagers.setId(null);
                branchManagers.setBranchIds(ufsUser.getBranchIds());
                branchManagers.setRegionIds(ufsUser.getRegionIds());
                branchManagers.setUserIds(ufsUser.getUserId());
                userTypesService.saveBranchManagers(branchManagers);
                break;
            default:
                break;
        }
        return response;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper> approveActions(@Valid @RequestBody ActionWrapper<Long> ids) throws ExpectationFailed {

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + Arrays.asList(ids));
        ResponseEntity<ResponseWrapper> response = super.approveActions(ids);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return response;
        }

        List<UfsUser> mlkUsers = new ArrayList<>();
        for (Long id : ids.getIds()) {
            Optional<UfsUser> isThere = urepo.findById(id);
            if (isThere.isPresent()) {
                UfsUser user = isThere.get();
                if (user.getAction().equals(AppConstants.ACTIVITY_CREATE)) {
                    mlkUsers.add(user);
                }
                if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UPDATE)) {
                    List<BigDecimal> oldids = new ArrayList<>();
                    List<BigDecimal> newids;

                    try {
                        UfsUser entity = supportRepo.mergeChanges(id, isThere.get());

                        isThere.get().getUfsUserWorkgroupList().forEach(wg -> {
                            oldids.add(wg.getGroupId());
                        });

                        newids = entity.getWorkgroupIds();

                        workGroupService.deleteAllByUserId(isThere.get().getUserId());

                        List<UfsUserWorkgroup> wkgroups = new ArrayList<>();

                        if (!(newids == null)) {
                            newids.forEach(wkgroup -> {
                                UfsUserWorkgroup newwkgroup = new UfsUserWorkgroup();
                                newwkgroup.setUserId(isThere.get().getUserId());
                                newwkgroup.setGroupId(wkgroup);
                                wkgroups.add(newwkgroup);

                            });


                            workGroupService.saveAll(wkgroups);
                        }


                    } catch (IOException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_DEACTIVATE)) {
                    user.setStatus(AppConstants.STATUS_DEACTIVATE);
                    userRepository.save(user);
                }

                if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_ACTIVATION)) {
                    user.setStatus(AppConstants.STATUS_ACTIVE);
                    userRepository.save(user);
                }

                if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_LOCKING)) {
                    user.setStatus(AppConstants.STATUS_LOCKED);
                    user.setActionStatus(AppConstants.STATUS_APPROVED);
                    userRepository.save(user);
                }

                if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UNLOCKING)) {
                    user.setStatus(AppConstants.STATUS_ACTIVE);
                    user.setActionStatus(AppConstants.STATUS_APPROVED);
                    userRepository.save(user);
                }
            }
        }

        if (mlkUsers.size() > 0) {
            urepo.processApprove(mlkUsers);
        }
        return response;

    }

    /*Getting User Workgroups */
    @RequestMapping("/workgroups/{id}")
    public ResponseEntity<ResponseWrapper> workgroups(@PathVariable("id") Long id) {
        ResponseWrapper response = new ResponseWrapper();
        List<Object> userWorkgroups = new ArrayList();


        List<UfsUserWorkgroup> userWorkgroupsDb = userWrkgroupRepo.findAllByUserId(id);

        if (userWorkgroupsDb.isEmpty()) {

            response.setData(userWorkgroups);
            return ResponseEntity.ok(response);
        }
        userWorkgroupsDb.forEach(wrkGroup -> {

            Map<String, Object> r = new HashMap<>();
            r.put("groupId", wrkGroup.getWorkgroup().getGroupId());
            r.put("groupName", wrkGroup.getWorkgroup().getGroupName());

            userWorkgroups.add(r);
        });


        response.setData(userWorkgroupsDb);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login-field-agent")
    public ResponseEntity<ResponseWrapper> userLogin(@RequestParam String username, @RequestParam String password) {
        String s = "http://localhost:" + port + "/api/v1/oauth/token";
        ResponseWrapper wrapper = new ResponseWrapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String app_payload = client_id + ":" + client_secret;

        byte[] auth = Base64.encodeBase64(app_payload.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(auth);
        headers.add("authorization", authHeader);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");

        ResponseEntity<OauthResponse> oauthResponse = oauthResponseHttpCall.sendAPIGatewayPOSTRequest(s, map, headers, OauthResponse.class);

        if (oauthResponse.getStatusCode().equals(HttpStatus.OK)) {
            wrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());
            wrapper.setMessage("Data fetched Successfully");
            wrapper.setCode(HttpStatus.OK.value());
            wrapper.setData(oauthResponse.getBody());
        }

        return ResponseEntity.ok(wrapper);
    }

    @Transactional
    @RequestMapping(value = "/lock-account", method = RequestMethod.POST)
    @ApiOperation(value = "Lock User", notes = "Lock multiple user accounts.")
    public ResponseEntity<ResponseWrapper> lockAccount(@RequestBody ActionWrapper<Long> accounts) {
        ResponseWrapper response = new ResponseWrapper();
        for (Long id : accounts.getIds()) {
            UfsUser user = urepo.findById(id).get();
            user.setAction(AppConstants.ACTIVITY_LOCKING);
            user.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            urepo.save(user);
            //to call logger service to save notes and approval status
            loggerService.log("Successfully to locked User with specified id",
                    UfsUser.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_AMEND, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, accounts.getNotes());

        }
        response.setMessage("User account(s) locked successfully");

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/unlock-account", method = RequestMethod.POST)
    @ApiOperation(value = "Unlock User", notes = "Unlock multiple user accounts.")
    public ResponseEntity<ResponseWrapper> unLockAccount(@RequestBody ActionWrapper<Long> accounts) {
        ResponseWrapper response = new ResponseWrapper();

        for (Long id : accounts.getIds()) {
            UfsUser user = urepo.findById(id).get();
            user.setAction(AppConstants.ACTIVITY_UNLOCKING);
            user.setActionStatus(AppConstants.STATUS_UNAPPROVED);
            urepo.save(user);
            //to call logger service to save notes and approval status
            loggerService.log("Successfully to Unlocked User with specified id",
                    UfsUser.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_AMEND, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, accounts.getNotes());

        }

        response.setMessage("User account(s) unlocked successfully");

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/decline-action-lock", method = RequestMethod.POST)
    @ApiOperation(value = "Decline Action", notes = "Decline action Lock account")
    public ResponseEntity<ResponseWrapper> declineActionLockAccount(@RequestBody ActionWrapper<Long> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<Long> errors = new ArrayList<>();

        for (Long id : action.getIds()) {
            UfsUser user = urepo.findById(id).get();

            Boolean isInitiator = loggerService.isInitiator(UfsUser.class.getSimpleName(),id,AppConstants.ACTIVITY_AMEND);

            if (isInitiator) {
                errors.add(id);
                continue;
            }

            if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UNLOCKING)) {
                user.setActionStatus(AppConstants.STATUS_REJECTED);
            }

            if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_LOCKING)) {
                user.setActionStatus(AppConstants.STATUS_REJECTED);
            }

//            user.setStatus(AppConstants.STATUS_ACTIVE);
//            user.setActionStatus(AppConstants.STATUS_APPROVED);
            urepo.save(user);

            loggerService.log("Successfully to Unlocked User with specified id",
                    UfsUser.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.ACTIVITY_DECLINE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, action.getNotes());

        }

        if (!errors.isEmpty()) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage("Failed to approve record. Maker can't approve their own record");
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }

        response.setMessage("Decline Action Lock Account");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/approve-action-lock", method = RequestMethod.POST)
    @ApiOperation(value = "Approve Action", notes = "Approve action Lock account")
    public ResponseEntity<ResponseWrapper> approveActionLockAccount(@RequestBody ActionWrapper<Long> action) {
        ResponseWrapper response = new ResponseWrapper();
        List<Long> errors = new ArrayList<>();
        for (Long id : action.getIds()) {
            UfsUser user = urepo.findById(id).get();

            Boolean isInitiator = loggerService.isInitiator(UfsUser.class.getSimpleName(),id,AppConstants.ACTIVITY_AMEND);

            if (isInitiator) {
                errors.add(id);
                continue;
            }

            if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_UNLOCKING)) {
                user.setStatus(AppConstants.STATUS_ACTIVE);
                user.setActionStatus(AppConstants.STATUS_APPROVED);
            }

            if (user.getAction().equalsIgnoreCase(AppConstants.ACTIVITY_LOCKING)) {
                user.setStatus(AppConstants.STATUS_LOCKED);
                user.setActionStatus(AppConstants.STATUS_APPROVED);
            }
            urepo.save(user);

            loggerService.log("Successfully to Unlocked User with specified id",
                    UfsUser.class.getSimpleName(), id, ke.axle.chassis.utils.AppConstants.STATUS_APPROVED, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, action.getNotes());

        }

        if (!errors.isEmpty()) {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage("Failed to approve record. Maker can't approve their own record");
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }

        response.setMessage("Approve Action Lock Account");

        return new ResponseEntity(response, HttpStatus.OK);
    }


    @Transactional
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    @ApiOperation(value = "Reset user password at login", notes = "Used when --forgot password-- is used. "
            + "Fetches user by email, resets password and send to email")
    public ResponseEntity<ResponseWrapper> forgotPassword(@RequestParam("email") String email) {
        String phoneNo = configRepo.getConfiguration("RA Bio", "raPhoneNumber").getValue();
        ResponseWrapper response = new ResponseWrapper();
        UfsAuthentication dbAuth = authRepository.findByusername(email);

        if (dbAuth != null) {
            String password = gen.generateRandomPassword();
            dbAuth.setPassword(passwordEncoder.encode(password));
            dbAuth.setPasswordStatus(AppConstants.STATUS_EXPIRED);
            authRepository.save(dbAuth);

            this.notifyService.sendEmail(dbAuth.getUsername(), "PASSWORD CHANGE REQUEST", "Use this  generate password to access your account: " + password + " If this wasn't you, kindly contact us on " + phoneNo);

            response.setMessage("Password reset successfully. Check your email for new credentials");
            loggerService.log("Password reset successfully",
                    UfsAuthentication.class.getSimpleName(), dbAuth.getAuthenticationId(), ke.axle.chassis.utils.AppConstants.ACTIVITY_UPDATE, ke.axle.chassis.utils.AppConstants.STATUS_COMPLETED, "");

            return new ResponseEntity(response, HttpStatus.OK);

        }
        response.setCode(404);
        response.setMessage("Provided email address doesnt exist. Check if the email provided is correct");
        return new ResponseEntity(response, HttpStatus.OK);

    }

    @ApiOperation(value = "Deleting A User")
    @RequestMapping(value = "/delete-action", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 207, message = "Some records could not be processed successfully")})
    @Transactional
    public ResponseEntity<ResponseWrapper> deleteUser(@Valid @RequestBody ActionWrapper<Long> actions) {
        ResponseWrapper response = new ResponseWrapper();
        List<String> errors = new ErrorList();

        for (Long id : actions.getIds()) {
            UfsUser user = urepo.findByUserIdAndIntrash(id);
            if (Objects.isNull(user)) {
                this.loggerService.log("Failed to delete user.Record has unapproved actions", UfsUser.class.getSimpleName(), null, "Deletion", "Failed", "");
                errors.add("User with id " + id + " doesn't exist");
            } else if (!user.getActionStatus().isEmpty() && user.getActionStatus().equalsIgnoreCase(ke.axle.chassis.utils.AppConstants.STATUS_UNAPPROVED)) {
                this.loggerService.log("Failed to delete user. Record has unapproved actions", UfsUser.class.getSimpleName(), id, "Deletion", "Failed", "");
                errors.add("Record has unapproved actions");

            } else {
                user.setAction(ke.axle.chassis.utils.AppConstants.ACTIVITY_DELETE);
                user.setActionStatus(ke.axle.chassis.utils.AppConstants.STATUS_UNAPPROVED);
                urepo.save(user);
                this.loggerService.log(user.getFullName() + " Deleted Successfully by " + logExtras.getFullName(), UfsUser.class.getSimpleName(), id, "Deletion", "Completed", "");
            }

        }

        if (errors.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            response.setCode(HttpStatus.MULTI_STATUS.value());
            response.setData(errors);
            response.setMessage("Some Actions could not be processed successfully check audit logs for more details");
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
        }
    }

    @RequestMapping(value = "/deactivate-account", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> deactivateAccount(@RequestBody DeactivateAccountWrapper deactivateAccountWrapper) {
        ResponseWrapper wrapper = new ResponseWrapper();
        deactivateAccountWrapper.ids.parallelStream().forEach(ids -> {
            UfsUser user = userRepository.findByUserIdAndIntrash(ids, AppConstants.INTRASH_NO);
            user.setAction(AppConstants.ACTIVITY_DEACTIVATE);
            user.setActionStatus(AppConstants.STATUS_UNAPPROVED);

            userRepository.save(user);

            loggerService.log("Deactivate User Account", UfsUser.class.getSimpleName(), ids, AppConstants.ACTIVITY_DEACTIVATE, AppConstants.STATUS_COMPLETED, deactivateAccountWrapper.getNotes());

        });

        wrapper.setMessage("Account deactivated");
        wrapper.setCode(HttpStatus.OK.value());

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @RequestMapping(value = "/activate-account", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper> activateAccount(@RequestBody DeactivateAccountWrapper deactivateAccountWrapper) {
        ResponseWrapper wrapper = new ResponseWrapper();
        deactivateAccountWrapper.ids.parallelStream().forEach(ids -> {
            UfsUser user = userRepository.findByUserIdAndIntrash(ids, AppConstants.INTRASH_NO);
            user.setAction(AppConstants.ACTIVITY_ACTIVATION);
            user.setActionStatus(AppConstants.STATUS_UNAPPROVED);

            userRepository.save(user);

            loggerService.log("Activate User Account", UfsUser.class.getSimpleName(), ids, AppConstants.ACTIVITY_ACTIVATION, AppConstants.STATUS_COMPLETED, deactivateAccountWrapper.getNotes());

        });

        wrapper.setMessage("Account Activated");
        wrapper.setCode(HttpStatus.OK.value());

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    private Date tryParse(String dateString) {
        List<String> formatStrings = Arrays.asList(
                "dd/MM/yyyy", "yyyy-MM-dd", "dd-MM-yyyy",
                "dd/MM/yyyy HH:mm:ss.SSS", "dd/MM/yyyy HH:mm:ss",
                "dd-MM-yyyy HH:mm:ss.SSS", "dd-MM-yyyy HH:mm:ss");
        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            } catch (ParseException e) {
            }
        }

        return null;
    }
}
