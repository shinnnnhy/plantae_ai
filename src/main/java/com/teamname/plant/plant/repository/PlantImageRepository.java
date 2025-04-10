package com.teamname.plant.plant.repository;

import com.teamname.plant.plant.model.PlantImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantImageRepository extends MongoRepository<PlantImage, String> {


    List<PlantImage> findByUserId(String userId);
}
