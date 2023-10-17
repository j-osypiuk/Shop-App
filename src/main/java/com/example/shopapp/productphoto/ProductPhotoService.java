package com.example.shopapp.productphoto;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductPhotoService {

    String savePhotos(Long id, MultipartFile[] files) throws IOException;
    String deleteAllPhotos(Long id);
    byte[] getPhotoByName(String fileName) throws IOException;
}
