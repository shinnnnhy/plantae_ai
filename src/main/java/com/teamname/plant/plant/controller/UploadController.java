package com.teamname.plant.plant.controller;

import com.teamname.plant.plant.model.PlantImage;
import com.teamname.plant.plant.repository.PlantImageRepository;
import com.teamname.plant.plant.service.GCSUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private GCSUploadService gcsUploadService;

    @Autowired
    private PlantImageRepository plantImageRepository;

    // ✅ JWT에서 userId 추출 유틸이 있다고 가정
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             HttpServletRequest request) {
        try {
            // 1. GCS에 이미지 업로드
            String imageUrl = gcsUploadService.uploadFile(file);

            // 2. JWT 필터에서 userId 파싱
            String userId = (String) request.getAttribute("userId");

            // 3. DB에 저장
            PlantImage plantImage = new PlantImage(userId, imageUrl, LocalDateTime.now());
            plantImageRepository.save(plantImage);

            return "업로드 및 저장 완료: " + imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "업로드 실패: " + e.getMessage();
        }
    }

}
