package com.teamname.plant.plant.controller;

import com.teamname.plant.plant.model.PlantImage;
import com.teamname.plant.plant.repository.PlantImageRepository;
import com.teamname.plant.plant.service.GCSUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.List;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private GCSUploadService gcsUploadService;

    @Autowired
    private PlantImageRepository plantImageRepository;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("cropType") String cropType,
                             HttpServletRequest request) {
        try {
            String imageUrl = gcsUploadService.uploadFile(file);
            String userId = (String) request.getAttribute("userId");

            PlantImage plantImage = new PlantImage();
            plantImage.setUserId(userId);
            plantImage.setImageUrl(imageUrl);
            plantImage.setUploadedAt(LocalDateTime.now());
            plantImage.setCropType(cropType);  // 선택된 작물 저장

            plantImageRepository.save(plantImage);

            return "업로드 및 저장 완료: " + imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "업로드 실패: " + e.getMessage();
        }
    }


    // 사용자별 업로드 이미지 조회 API 추가
    @GetMapping("/images")
    public ResponseEntity<List<PlantImage>> getUserImages(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<PlantImage> images = gcsUploadService.getImagesByUserId(userId);
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable String id, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        return plantImageRepository.findById(id)
                .map(image -> {
                    if (!image.getUserId().equals(userId)) {
                        return ResponseEntity.status(403).body("본인 이미지만 삭제할 수 있습니다.");
                    }

                    try {
                        gcsUploadService.deleteFileFromGCS(image.getImageUrl());
                        plantImageRepository.deleteById(id);
                        return ResponseEntity.ok("이미지 삭제 완료");
                    } catch (Exception e) {
                        return ResponseEntity.status(500).body("삭제 실패: " + e.getMessage());
                    }
                })
                .orElse(ResponseEntity.status(404).body("해당 이미지가 없습니다."));
    }
    @GetMapping("/admin/images")
    public ResponseEntity<?> getAllImages(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        // 관리자 계정 이메일 기준으로 검증 (임시 방식)
        if (!"admin@gmail.com".equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }

        List<PlantImage> images = plantImageRepository.findAll();
        return ResponseEntity.ok(images);
    }

}
