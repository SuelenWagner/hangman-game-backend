package com.hangmangame.hangmangame.controller;

import com.hangmangame.hangmangame.model.Category;
import com.hangmangame.hangmangame.model.Word;
import com.hangmangame.hangmangame.repository.WordRepository;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class WordController {

    @Autowired
    WordRepository wordRepository;
    List<Word> wordsMode = new ArrayList<Word>();


    @GetMapping("/words")
    public ResponseEntity<List<Word>> getAllWords(@RequestParam(required = false) String name) {
        try {
            List<Word> words = new ArrayList<Word>();

            if (name == null) {
                wordRepository.findAll(Sort.by(Sort.Direction.ASC,"name")).forEach(words::add);
            } else {
                wordRepository.findByNameContaining(name).forEach(words::add);
            }

            if (words.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(words, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/words/{id}")
    public ResponseEntity<Word> getWordById(@PathVariable("id") Integer id) {
        Optional<Word> wordData = wordRepository.findById(id);

        if (wordData.isPresent()) {
            return new ResponseEntity<>(wordData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/words")
    public ResponseEntity<Word> createWord(@RequestBody Word word) {
        try {

            Word _word = wordRepository
                    .save(word);
            return new ResponseEntity<>(_word, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/words/{id}")
    public ResponseEntity<Word> updateWord(@PathVariable("id") Integer id, @RequestBody Word word) {
        Optional<Word> wordData = wordRepository.findById(id);

        if (wordData.isPresent()) {
            Word _word = wordData.get();
            _word.setName(word.getName());
            _word.setCategory(word.getCategory());
            return new ResponseEntity<>(wordRepository.save(_word), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/words/{id}")
    public ResponseEntity<HttpStatus> deleteWord(@PathVariable("id") Integer id) {
        try {
            wordRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/words/mode/{id}")
    public ResponseEntity<List<Word>> getWordByCategory(@PathVariable("id") int category) {
        try {
            wordsMode.clear();
            List<Word> _words = new ArrayList<Word>(wordRepository.findAll());

            for (Word x : _words) {
                if (x.getCategory().getId() == category) {
                    wordsMode.add(x);
                }
            }
            return new ResponseEntity<>(wordsMode, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/words/mode/random")
    public ResponseEntity<List<Word>> getWordRandomly() {
        try {
            wordsMode.clear();
            wordRepository.findAll().forEach(wordsMode::add);
            return new ResponseEntity<>(wordsMode, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
