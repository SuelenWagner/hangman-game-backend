package com.hangmangame.hangmangame.controller;

import com.hangmangame.hangmangame.model.Achievement;
import com.hangmangame.hangmangame.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//CRUD completo, mas não serão usados todos os métodos, pois o adminstrador não poderá adicionar mais conquistas

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AchievementController {

    @Autowired
    AchievementRepository achievementRepository;

    @GetMapping("/achievements")
    public ResponseEntity<List<Achievement>> getAllAchievements(@RequestParam(required = false) String title) {
        try {
            List<Achievement> achievements = new ArrayList<Achievement>();

            if (title == null) {
                achievementRepository.findAll(Sort.by(Sort.Direction.ASC,"title")).forEach(achievements::add);
            } else {
                achievementRepository.findByTitleContaining(title).forEach(achievements::add);
            }

            if (achievements.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(achievements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/achievements/{id}")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable("id") Integer id) {
        Optional<Achievement> achievementData = achievementRepository.findById(id);

        if (achievementData.isPresent()) {
            return new ResponseEntity<>(achievementData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/achievements")
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
        try {
            Achievement _achievement = achievementRepository
                    .save(achievement);
            return new ResponseEntity<>(_achievement, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/achievements/{id}")
    public ResponseEntity<Achievement> updateAchievement(@PathVariable("id") Integer id, @RequestBody Achievement achievement) {
        Optional<Achievement> achievementData = achievementRepository.findById(id);

        if (achievementData.isPresent()) {
            Achievement _achievement = achievementData.get();
            _achievement.setTitle(achievement.getTitle());
            _achievement.setDescription(achievement.getDescription());
            return new ResponseEntity<>(achievementRepository.save(_achievement), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/achievements/{id}")
    public ResponseEntity<HttpStatus> deleteAchievement(@PathVariable("id") Integer id) {
        try {
            achievementRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}