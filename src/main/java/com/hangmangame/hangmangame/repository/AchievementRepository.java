package com.hangmangame.hangmangame.repository;

import com.hangmangame.hangmangame.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Integer> {
    List<Achievement>findByTitleContaining(String title);
}
