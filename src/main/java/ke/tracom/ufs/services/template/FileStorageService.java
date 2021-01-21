/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.tracom.ufs.services.template;

import ke.tracom.ufs.config.FileStorageProperties;
import ke.tracom.ufs.utils.exceptions.FileStorageException;
import ke.tracom.ufs.repositories.UfsSysConfigRepository;
import ke.tracom.ufs.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author emuraya
 */
@Service
public class FileStorageService {

    private final UfsSysConfigRepository sysRepo;

    private final Path fileStorageLocation;


    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties, UfsSysConfigRepository sysRepo) {
        this.sysRepo = sysRepo;

        this.fileStorageLocation = Paths.get(this.sysRepo.uploadDir(AppConstants.PROFILE_PIC_ENTITY, AppConstants.PROFILE_PIC_PARAM)).toAbsolutePath().normalize();


        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {

        }
    }

    public String storeFile(MultipartFile file, String generated) throws Exception {
        String fileName = generated + StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry, Filename contains invalid path seqeunce " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        } catch (Exception e) {
            System.out.println("THROWING GENERAL EXCEPTION :.....");
            e.printStackTrace();
            throw new Exception();
        }


    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found ");
        }
    }

}
