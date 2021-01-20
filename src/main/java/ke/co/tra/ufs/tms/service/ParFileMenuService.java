package ke.co.tra.ufs.tms.service;

import ke.co.tra.ufs.tms.entities.wrappers.MenuFileRequest;

public interface ParFileMenuService {
    void generateMenuFileAsync(MenuFileRequest request, String filePath);
}
