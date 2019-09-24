package ke.tra.ufs.webportal.service;

import ke.tra.ufs.webportal.entities.UfsGlsBatch;
import ke.tra.ufs.webportal.utils.SharedMethods;
import ke.tra.ufs.webportal.wrappers.UfsGlsWrapper;

public interface GlsBatchUploadService {

    void processGlsFileUpload(UfsGlsBatch batch, SysConfigService configService, SharedMethods sharedMethods, byte[] file, String ipAddress, String userAgent, UfsGlsWrapper payload);
}
