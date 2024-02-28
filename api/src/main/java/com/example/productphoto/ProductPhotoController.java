package com.example.productphoto;

import com.example.exception.InvalidStateException;
import com.example.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photo")
public class ProductPhotoController {

    private final ProductPhotoService productPhotoService;

    public ProductPhotoController(ProductPhotoService productPhotoService) {
        this.productPhotoService = productPhotoService;
    }

    @PostMapping("/product/{id}")
    public ResponseEntity<Void> savePhotos(@PathVariable("id") Long id, @RequestParam MultipartFile[] photos) throws InvalidStateException, ObjectNotFoundException {
        productPhotoService.savePhotos(id, photos);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{name}")
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable("name") String photoName) throws ObjectNotFoundException, InvalidStateException {
        byte[] imageFile = productPhotoService.getPhotoByName(photoName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageFile);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteAllPhotos(@PathVariable("id") Long id) throws ObjectNotFoundException, InvalidStateException {
        productPhotoService.deleteAllPhotos(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
