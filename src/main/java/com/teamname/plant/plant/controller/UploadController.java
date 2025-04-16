package com.teamname.plant.plant.controller;

import com.teamname.plant.plant.model.PlantImage;
import com.teamname.plant.plant.repository.PlantImageRepository;
import com.teamname.plant.plant.service.GCSUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
                             HttpServletRequest request) {
        try {
            String imageUrl = gcsUploadService.uploadFile(file);
            String userId = (String) request.getAttribute("userId");

            PlantImage plantImage = new PlantImage(userId, imageUrl, LocalDateTime.now());
            plantImageRepository.save(plantImage);

            return "업로드 및 저장 완료: " + imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "업로드 실패: " + e.getMessage();
        }
    }

    // ✅ 사용자별 업로드 이미지 조회 API 추가
    @GetMapping("/images")
    public ResponseEntity<List<PlantImage>> getUserImages(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<PlantImage> images = gcsUploadService.getImagesByUserId(userId);
        return ResponseEntity.ok(images);
    }
}
