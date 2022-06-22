package com.packandgo.tripdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.packandgo.tripdiary.enums.UserStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    private String password;

    private String verifyToken;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToMany(cascade = {CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    @JoinTable(name = "user_trip", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "trip_id")})
    @JsonIgnore
    private List<Trip> trips = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Like> likes = new ArrayList<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.verifyToken = UUID.randomUUID().toString();
        this.status = UserStatus.INACTIVE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        if (this.trips == null) {
            trips = new ArrayList<>();
        }
        trips.stream().forEach(trip -> trip.setOwner(this.getUsername()));
        this.trips = trips;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void addTrip(Trip trip) {
        if (this.trips == null) {
            trips = new ArrayList<>();
        }
        this.trips.add(trip);
        trip.setOwner(this.username);
    }

    public void removeTrip(Trip trip) {
        if (!this.trips.isEmpty()) {
            this.trips.remove(trip);
            trip.getUsers().remove(this);
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' + ", verifyToken='" + verifyToken + '\'' + ", status=" + status + ", roles=" + roles + '}';
    }


    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE;
    }
}
