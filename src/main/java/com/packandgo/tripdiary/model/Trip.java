package com.packandgo.tripdiary.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.packandgo.tripdiary.enums.Transportation;
import com.packandgo.tripdiary.enums.TripStatus;
import com.packandgo.tripdiary.payload.request.trip.TripRequest;
import com.packandgo.tripdiary.payload.response.TripResponse;
import com.packandgo.tripdiary.util.ListStringConverter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "destination_id",
            referencedColumnName = "id"
    )
    private Destination destination;

    @Column(name = "begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date beginDate;

    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Transportation transportation;

    @Column(name = "prepared_list")
    @Convert(converter = ListStringConverter.class)
    private List<String> preparedList;

    @Enumerated(EnumType.STRING)
    private TripStatus status;


    @Column
    @Type(type = "text")
    private String note;

    @Column(name = "notify_before")
    private int notifyBefore;

    @ManyToMany(
            mappedBy = "trips",
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    })
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "trip",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VisitDay> visitDays;

    private String owner;

    public String concurrencyUnit;

    @OneToMany(mappedBy = "trip",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PriceItem> priceList;


    public Trip() {
        priceList = new ArrayList<>();
        visitDays = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void mapping(TripRequest request) {
        this.setName(request.getName());
        this.setThumbnailUrl(request.getThumbnailUrl());
        this.setDestination(request.getDestination());
        this.setName(request.getName());
        this.setBeginDate(request.getBeginDate());
        this.setEndDate(request.getEndDate());

        //set transportation
        if (request.getTransportation() == null) {
            this.setTransportation(Transportation.UNDEFINED);
        } else {
            switch (request.getTransportation()) {
                case "car": {
                    this.setTransportation(Transportation.CAR);
                    break;
                }
                case "bus": {
                    this.setTransportation(Transportation.BUS);
                    break;
                }
                case "train": {
                    this.setTransportation(Transportation.TRAIN);
                    break;
                }
                case "plane": {
                    this.setTransportation(Transportation.PLANE);
                    break;
                }
                case "motorbike": {
                    this.setTransportation(Transportation.MOTORBIKE);
                    break;
                }
                case "bike": {
                    this.setTransportation(Transportation.BIKE);
                    break;
                }
                case "ship": {
                    this.setTransportation(Transportation.SHIP);
                    break;
                }
                case "on_walk": {
                    this.setTransportation(Transportation.ON_WALK);
                    break;
                }
                default:
                    this.setTransportation(Transportation.UNDEFINED);
                    break;
            }
        }

        TripStatus status = request.getStatus().toLowerCase().equals("private") ?
                TripStatus.PRIVATE : TripStatus.PUBLIC;

        this.setStatus(status);
        this.setNotifyBefore(request.getNotifyBefore());

        this.setVisitDays(request.getVisitDays());
        this.setPriceList(request.getPriceList());
        this.setPreparedList(request.getPreparedList());
        this.setNote(request.getNote());

        if (request.getConcurrencyUnit() == null ||
                request.getConcurrencyUnit().trim().length() == 0) {
            this.setConcurrencyUnit("$");
        } else {
            this.setConcurrencyUnit(request.getConcurrencyUnit());
        }


    }

    public TripResponse toResponse() {
        TripResponse tripResponse = new TripResponse();
        tripResponse.setId(this.getId());
        tripResponse.setName(this.getName());
        tripResponse.setThumbnailUrl(this.getThumbnailUrl());
        tripResponse.setDestination(this.getDestination());
        tripResponse.setBeginDate(this.getBeginDate());
        tripResponse.setEndDate(this.getEndDate());
        tripResponse.setTransportation(this.getTransportation().toString());
        tripResponse.setPreparedList(this.getPreparedList());
        tripResponse.setStatus(this.getStatus().toString());
        tripResponse.setNote(this.getNote());
        tripResponse.setConcurrencyUnit(this.getConcurrencyUnit());
        tripResponse.setVisitDays(this.getVisitDays());
        tripResponse.setPriceList(this.getPriceList());
        tripResponse.setNotifyBefore(this.getNotifyBefore());
        tripResponse.setNumOfLikes(0);
        tripResponse.setOwner(this.owner);
        this.users.forEach(user -> {
            if (!user.getUsername().equals(owner))
                tripResponse.getTripMates().add(user.getUsername());
        });
        return tripResponse;
    }

    public void addVisitDay(VisitDay visitDay) {
        if (this.visitDays == null) {
            visitDays = new ArrayList<>();
        }
        this.visitDays.add(visitDay);
        visitDay.setTrip(this);
    }

    public void addPriceItem(PriceItem item) {
        if (this.priceList == null) {
            priceList = new ArrayList<>();
        }
        this.priceList.add(item);
        item.setTrip(this);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getNotifyBefore() {
        return notifyBefore;
    }

    public void setNotifyBefore(int notifyBefore) {
        this.notifyBefore = notifyBefore;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getPreparedList() {
        return preparedList;
    }

    public void setPreparedList(List<String> preparedList) {
        this.preparedList = preparedList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public List<VisitDay> getVisitDays() {
        return visitDays;
    }

    public void setVisitDays(List<VisitDay> visitDays) {
        visitDays.forEach(day -> day.setTrip(this));
        if (this.visitDays != null) {
            this.visitDays.clear();
        }
        this.visitDays.addAll(visitDays);
    }

    public List<PriceItem> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceItem> priceList) {
        priceList.forEach(item -> item.setTrip(this));
        if (this.priceList == null) {
            priceList = new ArrayList<>();
        }

        this.priceList.clear();
        this.priceList.addAll(priceList);
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public String getConcurrencyUnit() {
        return concurrencyUnit;
    }

    public void setConcurrencyUnit(String concurrencyUnit) {
        this.concurrencyUnit = concurrencyUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        if (this.getUsers() == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
        user.getTrips().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getTrips().remove(this);
    }
}
