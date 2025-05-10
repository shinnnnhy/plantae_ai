package com.teamname.plant.plant.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "plant_images")
public class PlantImage {

    @Id
    private String id;

    private String userId;
    private String imageUrl;
    private LocalDateTime uploadedAt;
    private String cropType;

    public PlantImage() {}

    public PlantImage(String userId, String imageUrl, LocalDateTime uploadedAt, String cropType) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.uploadedAt = uploadedAt;
        this.cropType = cropType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }
}


