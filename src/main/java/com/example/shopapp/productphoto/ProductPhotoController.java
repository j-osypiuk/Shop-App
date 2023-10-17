package com.example.shopapp.productphoto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/photos")
public class ProductPhotoController {

    @Autowired
    ProductPhotoService productPhotoService;

    @PostMapping("/product/{id}")
    public String savePhotos(@PathVariable("id") Long id, @RequestParam MultipartFile[] photos) throws IOException {
        return productPhotoService.savePhotos(id, photos);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> downloadFile(@PathVariable("name") String fileName) throws IOException {
        byte[] imageFile = productPhotoService.getPhotoByName(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageFile);
    }

    @DeleteMapping("/product/{id}")
    public String deleteAllPhotos(@PathVariable("id") Long id) {
        return productPhotoService.deleteAllPhotos(id);
    }
}
