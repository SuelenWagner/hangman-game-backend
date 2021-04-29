package com.hangmangame.hangmangame.repository;

import com.hangmangame.hangmangame.model.Ranking;
import com.hangmangame.hangmangame.model.Word;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RankingRepository  extends JpaRepository<Ranking, Integer> {
    List<Ranking> findByPlayerNameContaining(String playerName);
}