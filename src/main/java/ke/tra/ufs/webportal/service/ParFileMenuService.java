package ke.tra.ufs.webportal.service;


import ke.tra.ufs.webportal.entities.wrapper.MenuFileRequest;

public interface ParFileMenuService {
    void generateMenuFileAsync(MenuFileRequest request, String filePath);
}
