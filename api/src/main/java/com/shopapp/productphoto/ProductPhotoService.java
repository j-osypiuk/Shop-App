package com.shopapp.productphoto;

import com.shopapp.exception.InvalidStateException;
import com.shopapp.exception.ObjectNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public interface ProductPhotoService {

    void savePhotos(Long id, MultipartFile[] files) throws InvalidStateException, ObjectNotFoundException;
    void deleteAllPhotos(Long id) throws ObjectNotFoundException, InvalidStateException;
    byte[] getPhotoByName(String fileName) throws ObjectNotFoundException, InvalidStateException;
}
