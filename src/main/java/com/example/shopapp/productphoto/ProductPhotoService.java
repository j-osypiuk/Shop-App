package com.example.shopapp.productphoto;

import com.example.shopapp.error.exception.InvalidStateException;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public interface ProductPhotoService {

    void savePhotos(Long id, MultipartFile[] files) throws InvalidStateException, ObjectNotFoundException;
    void deleteAllPhotos(Long id) throws ObjectNotFoundException, InvalidStateException;
    byte[] getPhotoByName(String fileName) throws ObjectNotFoundException, InvalidStateException;
}
