package com.packandgo.tripdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String url;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_place_id", referencedColumnName = "id")
    @JsonIgnore
    private VisitPlace visitPlace;


    public Image(){};

    public Image(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VisitPlace getVisitPlace() {
        return visitPlace;
    }

    public void setVisitPlace(VisitPlace visitPlace) {
        this.visitPlace = visitPlace;
    }
}
