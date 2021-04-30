package com.hangmangame.hangmangame.controller;

import com.hangmangame.hangmangame.model.Ranking;
import com.hangmangame.hangmangame.repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RankingController {

    @Autowired
    RankingRepository rankingRepository;

    //O ranking deve listar apenas os 10 primeiros jogadores
    @GetMapping("/rankings")
    public ResponseEntity<List<Ranking>> getAllRanking(@RequestParam(required = false) String name) {
        try {
            List<Ranking> rankingList = new ArrayList<Ranking>();


            if (name == null) {
                rankingRepository.findAll().forEach(rankingList::add);
            } else {
                rankingRepository.findByPlayerContaining(name).forEach(rankingList::add);
            }

            if (rankingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Collections.sort(rankingList, (c1, c2) -> {
                int cmp = Integer.compare(c2.getScore(), c1.getScore());
                if (cmp == 0) { // se as potuações forem iguais, desempata pela maior quantidade de conquistas
                    cmp = Double.compare(c2.getAchievements().size(), c1.getAchievements().size());
                    if (cmp == 0) { // se a quantidade de conquistas forem iguais, desempata pela menor data
                        cmp = c2.getCurrentDate().compareTo(c1.getCurrentDate());
                    }
                }
                return cmp;
            });

            List<Ranking> rankingList2 = rankingList.stream().limit(10).collect(Collectors.toList());

            return new ResponseEntity<>(rankingList2, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rankings/{id}")
    public ResponseEntity<Ranking> getRankingById(@PathVariable("id") Integer id) {
        Optional<Ranking> rankingData = rankingRepository.findById(id);

        if (rankingData.isPresent()) {
            return new ResponseEntity<>(rankingData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/rankings")
    public ResponseEntity<Ranking> createRanking(@RequestBody Ranking ranking) {
        try {
            Calendar temp = Calendar.getInstance();
            ranking.setCurrentDate(temp);

            Ranking _ranking = rankingRepository
                    .save(ranking);
            return new ResponseEntity<>(_ranking, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    @PutMapping("/ranking/{id}")
    public ResponseEntity<Ranking> updateRanking(@PathVariable("id") Integer id, @RequestBody Ranking ranking) {
        Optional<Ranking> rankingData = rankingRepository.findById(id);

        if (rankingData.isPresent()) {
            Ranking _ranking = rankingData.get();
            _ranking.setName(ranking.getName().toUpperCase());
            _ranking.setCategory(ranking.getCategory());
            return new ResponseEntity<>(wordRepository.save(_word), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/words/{id}")
    public ResponseEntity<HttpStatus> deleteRanking(@PathVariable("id") Integer id) {
        try {
            wordRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */
}
