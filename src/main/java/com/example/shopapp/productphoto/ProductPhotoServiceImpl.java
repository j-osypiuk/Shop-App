package com.example.shopapp.productphoto;

import com.example.shopapp.exception.InvalidStateException;
import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.product.Product;
import com.example.shopapp.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
public class ProductPhotoServiceImpl implements ProductPhotoService{

    private final ProductPhotoRepository productPhotoRepository;
    private final ProductRepository productRepository;
    private final String FOLDER_PATH = System.getProperty("user.dir")
            + "\\src\\main\\resources\\static\\images\\product_photos\\";

    public ProductPhotoServiceImpl(ProductPhotoRepository productPhotoRepository, ProductRepository productRepository) {
        this.productPhotoRepository = productPhotoRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void savePhotos(Long id, MultipartFile[] files) throws InvalidStateException, ObjectNotFoundException {
        Product productDB = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product with id = " + id + " not found"));

        int number = 0;
        for (MultipartFile file : files) {
            UUID photoName = UUID.randomUUID();
            String filePath = FOLDER_PATH + photoName.toString() + ".png";

            try {
                file.transferTo(new File(filePath));
                productPhotoRepository.save(ProductPhoto.builder()
                            .photoName(photoName)
                            .number(number)
                            .product(Product.builder().productId(id).build())
                            .build());
            } catch (IOException e) {
                throw new InvalidStateException("Cannot save file number = " + number + 1);
            }
            number++;
        }
    }

    @Override
    public void deleteAllPhotos(Long id) throws ObjectNotFoundException, InvalidStateException {
        List<ProductPhoto> productPhotos = productPhotoRepository.findAllPhotosByProductProductId(id);

        if (productPhotos.isEmpty()) throw new ObjectNotFoundException("Photos for product with id = " + id + " not found");

        for (ProductPhoto productPhoto : productPhotos) {
            File file = new File(FOLDER_PATH + productPhoto.getPhotoName() + ".png");
            if(file.delete()) {
                Integer isDeleted = productPhotoRepository.deletePhotoByPhotoName(productPhoto.getPhotoName());
                if (isDeleted == 0)
                    throw new InvalidStateException("Cannot delete photo with name = " + productPhoto.getPhotoName() + ".png");
            }
            else
                throw new InvalidStateException("Cannot delete photo with name = " + productPhoto.getPhotoName() + ".png");
        }
    }

    @Override
    public byte[] getPhotoByName(String photoName) throws ObjectNotFoundException, InvalidStateException {
        String filePath = FOLDER_PATH + photoName;
        File photoFile = new File(filePath);

        if (!photoFile.exists()) throw new ObjectNotFoundException("Photo with name = " + photoName + " not found");

        try {
            return Files.readAllBytes(photoFile.toPath());
        } catch (IOException e) {
            throw new InvalidStateException("Cannot read file with name = " + photoName);
        }
    }
}
