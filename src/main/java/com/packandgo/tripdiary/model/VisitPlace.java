package com.packandgo.tripdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visit_place")
public class VisitPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String address;
    private double longitude;
    private double latitude;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "visitday_id", referencedColumnName = "id")
    @JsonIgnore
    private VisitDay visitDay;

    @OneToMany(
            mappedBy = "visitPlace",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Image> images = new ArrayList<>();

    private String description;

    public VisitPlace() {
    }

    public VisitPlace(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void addImage(Image image) {
        if(this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
        image.setVisitPlace(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public VisitDay getVisitDay() {
        return visitDay;
    }

    public void setVisitDay(VisitDay visitDay) {
        this.visitDay = visitDay;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        if(this.images == null) {
            this.images = new ArrayList<>();
        }
        images.forEach(image -> image.setVisitPlace(this));
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
