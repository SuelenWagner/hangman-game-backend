package com.hangmangame.hangmangame.model;

import javax.persistence.*;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "ranking")
public class Ranking {

    //O ranking deve listar apenas os 10 primeiros jogadores

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "Nome não pode estar vazio")
    @Size(min = 2, max = 50, message = "Palavra deve possuir de 2 até 50 caracteres.")
    @Column(name = "player")
    private String player;

    @Column(name = "score")
    private int score;

    @ManyToMany
    @JoinTable(name="player_has_achievements", joinColumns=
            {@JoinColumn(name="ranking_id")}, inverseJoinColumns=
            {@JoinColumn(name="achievement_id")})
    private List<Achievement> achievements;

    @Column(name = "date")
    private Calendar currentDate;

    public Ranking() {
    }

    public Ranking(String player, int score, Calendar currentDate) {
        this.player = player;
        this.score = score;
        this.currentDate = currentDate;
    }

    public Ranking(String player, int score, List<Achievement> achievements, Calendar currentDate) {
        this.player = player;
        this.score = score;
        this.achievements = achievements;
        this.currentDate = currentDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Calendar getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Calendar currentDate) {
        this.currentDate = currentDate;
    }


    @Override
    public String toString() {
        return "Ranking{" +
                "id=" + id +
                ", player='" + player + '\'' +
                ", score=" + score +
                ", achievements=" + achievements +
                '}';
    }
}
