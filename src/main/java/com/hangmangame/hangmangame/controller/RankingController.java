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

    @GetMapping("/rankingInfoPosition/{id}")
    public ResponseEntity<String> getRankingByIdInfoPosition(@PathVariable("id") Integer id) {
        Optional<Ranking> rankingData = rankingRepository.findById(id);
        Ranking ranking = rankingData.get();

        if (rankingData.isPresent()) {

            List<Ranking> rankingList = new ArrayList<Ranking>();
            rankingRepository.findAll().forEach(rankingList::add);

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

            String position = "Posição: " + (rankingList.indexOf(ranking)+1) + " - " + ranking;

            return new ResponseEntity<>(position, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rankingPosition/{id}")
    public ResponseEntity<List<Ranking>> getRankingByIdPosition(@PathVariable("id") Integer id) {
        Optional<Ranking> rankingData = rankingRepository.findById(id);

        if (rankingData.isPresent()) {
            List<Ranking> rankingList = new ArrayList<Ranking>();
            rankingRepository.findAll().forEach(rankingList::add);

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

            Ranking ranking = rankingData.get();
            List<Ranking> rankingList2;

            if((rankingList.indexOf(ranking) >= rankingList.size()-6) && (rankingList.size() >= 10)){//posição no fim da lista (falta posições)
                rankingList2 = rankingList.subList(rankingList.lastIndexOf(ranking)-9,rankingList.lastIndexOf(ranking) + 1);
            }else if(rankingList.indexOf(ranking)<10){//posição no início da lista
                rankingList2 = rankingList.stream().limit(10).collect(Collectors.toList());
            }else{
                rankingList2 = rankingList.subList(rankingList.indexOf(ranking)-4,rankingList.indexOf(ranking)+6);
            }

            return new ResponseEntity<>(rankingList2, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/rankings")
    public ResponseEntity<Ranking> createRanking(@RequestBody Ranking ranking) {
        try {
            Calendar temp = Calendar.getInstance();
            ranking.setCurrentDate(temp);

            Ranking _ranking = new Ranking();
            _ranking.setPlayer("No nick");
            _ranking.setScore(0);
            _ranking.setAchievements(null);
            rankingRepository.save(_ranking);
            return new ResponseEntity<>(_ranking, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rankingTemp/{id}")
    public ResponseEntity<Ranking> updateScoreAndAchievements(@PathVariable("id") Integer id, @RequestBody Ranking ranking) {
        Optional<Ranking> rankingData = rankingRepository.findById(id);

        if (rankingData.isPresent()) {

            Ranking _ranking = rankingData.get();
            _ranking.setScore(ranking.getScore() + _ranking.getScore());
            _ranking.newAchievement(ranking.getAchievements());
            return new ResponseEntity<>(rankingRepository.save(_ranking), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/rankingSave/{id}")
    public ResponseEntity<Ranking> updateRanking(@PathVariable("id") Integer id, @RequestBody Ranking ranking) {
        Optional<Ranking> rankingData = rankingRepository.findById(id);

        if (rankingData.isPresent()) {
            Calendar temp = Calendar.getInstance();

            Ranking _ranking = rankingData.get();
            _ranking.setPlayer(ranking.getPlayer());
            _ranking.setScore(ranking.getScore() + _ranking.getScore());
            _ranking.newAchievement(ranking.getAchievements());
            _ranking.setCurrentDate(temp);
            return new ResponseEntity<>(rankingRepository.save(_ranking), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

/*
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
