package com.practice.ImageCrud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practice.ImageCrud.model.Image;
import com.practice.ImageCrud.repo.ImageRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final String uploadDir = "uploads/";

    @Autowired
    private ImageRepo imageRepository;

    // Create/Upload Image
    public Image uploadImage(MultipartFile file) throws IOException {
        // Ensure directory exists
        Path uploadPath = Paths.get(uploadDir);
        System.out.println(uploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // Save image metadata in DB
        Image image = new Image(fileName, filePath.toString());
        return imageRepository.save(image); // Save and return the saved entity
    }

    // Read/Retrieve Image
    public byte[] getImage(Long id) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new IOException("Image not found"));

        Path filePath = Paths.get(image.getFilePath());
        if (Files.exists(filePath)) {
            return Files.readAllBytes(filePath);
        }
        throw new IOException("File not found on the file system");
    }

    // Update Image
    public Image updateImage(Long id, MultipartFile newFile) throws IOException {
        Image oldImage = imageRepository.findById(id)
                .orElseThrow(() -> new IOException("Image not found"));

        // Delete the old image file from the file system
        Path oldFilePath = Paths.get(oldImage.getFilePath());
        if (Files.exists(oldFilePath)) {
            Files.delete(oldFilePath);
        }

        // Upload the new file and update metadata in the DB
        String newFileName = newFile.getOriginalFilename();
        Path newFilePath = Paths.get(uploadDir, newFileName);
        Files.write(newFilePath, newFile.getBytes());

        oldImage.setFileName(newFileName);
        oldImage.setFilePath(newFilePath.toString());
        return imageRepository.save(oldImage); // Save updated image metadata
    }

    // Delete Image
    public void deleteImage(Long id) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new IOException("Image not found"));

        // Delete file from the file system
        Path filePath = Paths.get(image.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Delete metadata from the DB
        imageRepository.deleteById(id);
    }
}

