package com.hangmangame.hangmangame.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import javax.persistence.ManyToMany;

@Entity
@Table(name = "achievements", uniqueConstraints={@UniqueConstraint(columnNames="title")})
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    @Column(name = "title")
    private String title;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "achievements", cascade = CascadeType.PERSIST)
    private List<Ranking> ranking;

    public Achievement() {

    }

    public Achievement(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
