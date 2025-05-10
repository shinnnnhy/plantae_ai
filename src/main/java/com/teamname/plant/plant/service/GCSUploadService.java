package com.teamname.plant.plant.service;

import com.google.cloud.storage.*;
import com.teamname.plant.plant.model.PlantImage;
import com.teamname.plant.plant.repository.PlantImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GCSUploadService {

    private final Storage storage;
    private final PlantImageRepository plantImageRepository;

    public GCSUploadService(Storage storage, PlantImageRepository plantImageRepository) {
        this.storage = storage;
        this.plantImageRepository = plantImageRepository;
    }

    //  이미지 업로드 + URL 반환
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        BlobId blobId = BlobId.of("plant-image-bucket", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        return "https://storage.googleapis.com/plant-image-bucket/" + fileName;
    }

    // 사용자별 업로드 이력 조회
    public List<PlantImage> getImagesByUserId(String userId) {
        return plantImageRepository.findByUserId(userId);
    }
    public void deleteFileFromGCS(String imageUrl) {
        String bucketName = "plant-image-bucket";
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        BlobId blobId = BlobId.of(bucketName, fileName);
        storage.delete(blobId);  // 실패 시 false 리턴됨 (예외 아님)
    }

}
