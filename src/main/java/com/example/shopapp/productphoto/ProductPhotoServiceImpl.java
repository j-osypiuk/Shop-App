package com.example.shopapp.productphoto;

import com.example.shopapp.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
public class ProductPhotoServiceImpl implements ProductPhotoService{

    @Autowired
    ProductPhotoRepository productPhotoRepository;

    private final String FOLDER_PATH = System.getProperty("user.dir")
            + "\\src\\main\\resources\\static\\images\\product_photos\\";

    @Override
    public String savePhotos(Long id, MultipartFile[] files) throws IOException {
        int number = 0;
        for (MultipartFile file : files) {
            UUID photoName = UUID.randomUUID();
            String filePath = FOLDER_PATH+photoName.toString()+".png";

            productPhotoRepository.save(ProductPhoto.builder()
                        .photoName(photoName)
                        .number(number)
                        .product(Product.builder().productId(id).build())
                        .build());

            file.transferTo(new File(filePath));
            number++;
        }
        return "Files saved successfully";
    }

    @Override
    public String deleteAllPhotos(Long id) {
        List<ProductPhoto> productPhotos = productPhotoRepository.findAllPhotosByProductProductId(id);
        productPhotoRepository.deletePhotoByProductProductId(id);

        for (ProductPhoto productPhoto : productPhotos) {
            File file = new File(FOLDER_PATH + productPhoto.getPhotoName() + ".png");
            if(!file.delete()) return "Cannot delete photo: " + productPhoto.getPhotoName() + ".png";
        }
        return "Photos deleted successfully";
    }

    @Override
    public byte[] getPhotoByName(String fileName) throws IOException {
        String filePath = FOLDER_PATH + fileName;
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
