package com.hangmangame.hangmangame.repository;

import com.hangmangame.hangmangame.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WordRepository  extends JpaRepository<Word, Integer> {
    List<Word> findByNameContaining(String name);
}
