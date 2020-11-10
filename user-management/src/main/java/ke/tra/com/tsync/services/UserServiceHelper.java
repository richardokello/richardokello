//package ke.tra.com.tsync.services;
//
//public class UserServiceHelper {
//    private ResponseWrapper validatePosRequest(PosUserWrapper wrapper, boolean b, UfsPosAuditLog auditLog) {
//        // common validator method for members that need its
//
//        log.info("******************---0"+ wrapper.getUsername());
//        ResponseWrapper responseWrapper = new ResponseWrapper();
//
//        // log Ip
//        try {
//            auditLog.setIpAddress(InetAddress.getLocalHost().getHostAddress());
//        } catch (UnknownHostException e) {
//            auditLog.setIpAddress("Unknown IP address");
//            auditLog.setNotes("Unknown IP address");
//
//        }
//        validateDeviceDetails(wrapper, responseWrapper, auditLog);
//        //  error will be set true if validateDeviceDetails fail
//
//        if(responseWrapper.getError()){
//            return responseWrapper;
//        }
//        // validation for login
//        if (b) {
//            if (wrapper.getUsername() == null) {
//                responseWrapper.setError(true);
//                responseWrapper.setMessage("username cannot be null.");
//                auditLog.setDescription("Username is null");
//                responseWrapper.setCode(AppConstants.POS_USERNAME_MISSING);
//                return responseWrapper;
//            }
//        }
//        auditLog.setOccurenceTime(new Date());
//        auditLog.setStatus(AppConstants.STATUS_FAILED);
//        auditLog.setEntityName("UfsPosUser");
//        auditLog.setClientId(AppConstants.CLIENT_ID);
//
//        UfsPosUserRepository ufsPosUserRepository = SpringContextBridge.services().getPOSUserRepo();
//        TmsDeviceRepository tmsRepo = SpringContextBridge.services().getTmsDeviceRepo();
//        log.info(">>>>>>>>*** "+ wrapper.getUsername()+ "NO"+wrapper.getSerialNumber() );
//
//
//        Optional<UfsPosUser> ufsPosUser = ufsPosUserRepository.findByUsernameIgnoreCaseAndIntrash(wrapper.getUsername(), "NO");
//        ufsPosUser.ifPresent(posUser -> {
//            // common to all methods so lets set them at a common place
//            auditLog.setEntityId(String.valueOf(ufsPosUser.get().getPosUserId()));
//            auditLog.setUserId(ufsPosUser.get().getPosUserId().longValue());
//            responseWrapper.setPosUser(ufsPosUser);
//
//        });
//
//
//        if (ufsPosUser.isEmpty()) {
//            // common to all methods so lets set them at a common place
//            responseWrapper.setMessage("User with username " + wrapper.getUsername() + "For this device not found");
//            auditLog.setNotes("User with username Not found");
//            auditLog.setDescription("User with username " + wrapper.getUsername() + " Not found for device with serial number "+wrapper.getSerialNumber());
//            responseWrapper.setCode(AppConstants.POS_USER_NOT_FOUND);
//            responseWrapper.setError(true);
//            return responseWrapper;
//        }
//
//
//        String posRole = ufsPosUser.get().getPosRole();
//        log.info(wrapper.getSerialNumber());
//
//        boolean admin = false;
//
//        switch (posRole.toUpperCase()){
//
//            case "ADMIN":
//            case "ADMINISTRATOR":
//            case "ACTIVATION SUPPORT":
//            case "SUPPORT":
//                admin = true;
//
//                break;
//            default:
//                log.info("WE ARE c");
//                String serialNo = ufsPosUser.get().getSerialNumber()==null?"-1":ufsPosUser.get().getSerialNumber();
//                String username = ufsPosUser.get().getUsername();
//                // validate serial number if the user is not from bank ie not in the list of admins group
//
//                log.info(serialNo.equals(wrapper.getSerialNumber()) && username.toUpperCase().equals(wrapper.getUsername().toUpperCase()));
//                if(!(serialNo.equals(wrapper.getSerialNumber()) &&
//                        username.toUpperCase().equals(wrapper.getUsername().toUpperCase()))){
//                    log.info("user exist....");
//
//                    responseWrapper.setMessage("User with username " + wrapper.getUsername() + " does not belong to this terminal");
//                    auditLog.setNotes("User with username " + wrapper.getUsername() + " Not found");
//                    responseWrapper.setCode(AppConstants.POS_USER_NOT_FOUND);
//
//                    responseWrapper.setError(true);
//                    return responseWrapper;
//
//                }
//        }
//
//
//        //Steps:
//        // check if a user exist with the username provided
//        // check if a device exist with the supplied serial number
//        // If it does do a lookout for the outlet the device belongs to
//        // if an out let exist then check if the user belongs to that outlet
//
//
//
//        TmsDevice tmsDevice;
//        try{
//            tmsDevice = tmsRepo.findBySerialNoAndIntrash(wrapper.getSerialNumber(), "NO");
//
//        }catch (NoSuchElementException e){
//            responseWrapper.setError(true);
//            responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_FOUND);
//            auditLog.setNotes("Terminal device  with"+wrapper.getSerialNumber() + "Not found");
//            responseWrapper.setMessage("Terminal device  with"+wrapper.getSerialNumber() + "Not found");
//            return responseWrapper;
//        }
//
//
//        if(!admin) {
//            if (Objects.nonNull(tmsDevice)) {
//                try {
//                    if (tmsDevice.getOutletId() != null && ufsPosUser.get().getDeviceId() != null) {
//                        log.info("****1");
//                        //validate if user belongs to this outlet which the pos terminal belongs
//                        if (ufsPosUser.get().getDeviceId().getOutletId() == null) {
//                            responseWrapper.setMessage(wrapper.getSerialNumber() + " not attached to any outlet");
//                            responseWrapper.setCode(AppConstants.POS_USER_NOT_ATTACHED_TO_OUTLET);
//                            auditLog.setNotes(wrapper.getSerialNumber() + " not attached to any outlet");
//                            auditLog.setDescription(wrapper.getUsername() + " not authorised to access terminal with serial number:" + wrapper.getSerialNumber() + " Device not attached to any outlet");
//                            responseWrapper.setError(true);
//                            return responseWrapper;
//                        } else if (ufsPosUser.get().getDeviceId().getOutletId() == null) {
//                            log.info("****3 ");
//                            responseWrapper.setMessage("terminal device not attached to an outlet");
//                            responseWrapper.setError(true);
//
//                            responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_ATTACHED_TO_OUTLET);
//                            auditLog.setNotes("Orphan terminal ");
//                            auditLog.setDescription("Terminal device not attached to an outlet");
//                            return responseWrapper;
//
//                        } else if (!ufsPosUser.get().getDeviceId().getOutletId().getId().equals(tmsDevice.getOutletId().getId())) {
//                            log.info("**** 4 ");
//                            responseWrapper.setMessage(wrapper.getUsername() + " does not belong to " + tmsDevice.getOutletId().getOutletName() + " outlet");
//                            responseWrapper.setCode(AppConstants.POS_USER_TERMINAL_NOT_SAME_OUTLET);
//                            auditLog.setNotes("access by " + wrapper.getUsername() + " denied");
//                            auditLog.setDescription(wrapper.getUsername() + " does not belong to " + tmsDevice.getOutletId().getOutletName() + " outlet");
//                            responseWrapper.setError(true);
//                            return responseWrapper;
//                        }
//                    }
//                    log.info("**** 5");
//
//                } catch (NullPointerException ex) {
//                    ex.printStackTrace();
//                    responseWrapper.setError(true);
//                    responseWrapper.setCode(AppConstants.POS_SERVER_ERROR);
//                    auditLog.setNotes("System error");
//                    responseWrapper.setMessage("System error");
//                    return responseWrapper;
//                }
//
//            } else {
//
//                responseWrapper.setMessage("This Terminal device is not authorised to transact, its either deleted or does not exist.");
//                // common to all methods so lets set them at a common place
//                auditLog.setNotes("Unauthorised to transact");
//                auditLog.setDescription("This Terminal device is not authorised to transact, its either deleted or not approved yet. Device SNO " + wrapper.getSerialNumber());
//                responseWrapper.setCode(AppConstants.POS_TERMINAL_NOT_FOUND);
//                responseWrapper.setError(true);
//                return responseWrapper;
//
//            }
//        }
//        return responseWrapper;
//    }
//
//}
