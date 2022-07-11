package com.packandgo.tripdiary.test.user;

import com.packandgo.tripdiary.enums.TripStatus;
import com.packandgo.tripdiary.model.Destination;
import com.packandgo.tripdiary.model.Role;
import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.repository.TripRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TripRepositoryTest {
    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("Test insert user successfully")
    @Rollback(false)
    @Order(0)
    public void saveTripTest1() {
        Trip trip = new Trip();
        trip.setName("Trip to HCM city");

        Destination destination = new Destination();
        destination.setAddress("Thu Duc Ho Chi Minh city");
        destination.setLatitude(10);
        destination.setLongitude(20);

        trip.setDestination(destination);
        trip.setBeginDate(new Date());
        trip.setEndDate(new Date());

        trip.setNotifyBefore(2);
        trip.setStatus(TripStatus.PUBLIC);
        trip.setConcurrencyUnit("VND");

        Trip savedTrip = tripRepository.save(trip);

        Assert.isTrue(savedTrip.getName() == trip.getName());
        Assert.isTrue(savedTrip.getId() == 1);

    }

    @Test
    @DisplayName("Test insert trip without name")
    @Order(1)
    public void saveTripTest2() {
        Trip trip = new Trip();

        Destination destination = new Destination();
        destination.setAddress("Thu Duc Ho Chi Minh city");
        destination.setLatitude(10);
        destination.setLongitude(20);

        trip.setDestination(destination);
        trip.setBeginDate(new Date());
        trip.setEndDate(new Date());

        trip.setNotifyBefore(2);
        trip.setStatus(TripStatus.PUBLIC);
        trip.setConcurrencyUnit("VND");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            tripRepository.save(trip);
        });

    }
    @Test
    @DisplayName("Test when insert trip without destination")
    @Order(2)
    public void saveTripTest3() {
        Trip newTrip = new Trip();
        newTrip.setName("Trip test");
        newTrip.setThumbnailUrl("/trip");
        newTrip.setDescription("memo");
        newTrip.setStatus(TripStatus.PUBLIC);

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            tripRepository.save(newTrip);
        });
    }

    @Test
    @DisplayName("Test when insert trip with specify id")
    @Order(3)
    public void saveTripTest4() {
        Destination destination = new Destination();
        destination.setAddress("adress");
        destination.setLatitude(300000.2);
        destination.setLongitude(2308.89989);

        Trip newTrip = new Trip();
        newTrip.setId(Long.valueOf(1));
        newTrip.setName("Trip test");
        newTrip.setThumbnailUrl("/trip");
        newTrip.setDescription("memo");
        newTrip.setDestination(destination);
        newTrip.setStatus(TripStatus.PUBLIC);
        tripRepository.save(newTrip);

        Optional<Trip> savedTrip = tripRepository.findById(newTrip.getId());

        //comparing
        assertTrue(savedTrip.isPresent());
        assertNotNull(savedTrip.get());
        assertEquals(savedTrip.get().getId(), newTrip.getId());
        assertEquals(savedTrip.get().getName(), newTrip.getName());
        assertEquals(savedTrip.get().getThumbnailUrl(), newTrip.getThumbnailUrl());
        assertNotNull(savedTrip.get().getDestination());
        assertEquals(savedTrip.get().getStatus(), TripStatus.PUBLIC);

        tripRepository.deleteAll();
    }


    @Test
    @DisplayName("Test get all trips")
    @Order(4)
    public void getAllTripTest() {

        Trip newtrip = new Trip();
        newtrip.setName("Trip to HN city");

        Destination destination = new Destination();
        destination.setAddress("HN city");
        destination.setLatitude(20);
        destination.setLongitude(30);

        newtrip.setDestination(destination);

        newtrip.setNotifyBefore(3);
        newtrip.setStatus(TripStatus.PUBLIC);
        newtrip.setConcurrencyUnit("USD");

        tripRepository.save(newtrip);

        List<Trip> trips = (List<Trip>) tripRepository.findAll();
        Trip trip0 = trips.get(0);

        Assertions.assertEquals(1, trip0.getId());
        Assertions.assertEquals("Trip to HCM city", trip0.getName());
        Assertions.assertEquals("HN city", destination.getAddress());
        Assertions.assertEquals(20, destination.getLatitude());
        Assertions.assertEquals(30, destination.getLongitude());
        Assertions.assertEquals(2, trip0.getNotifyBefore());
        Assertions.assertEquals("VND", trip0.getConcurrencyUnit());

    }

    @Test
    @DisplayName("Test when delete trip with the id is not exist")
    @Order(5)
    public void deleteTripTest1() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            tripRepository.deleteById(Long.valueOf(10000));
        });
    }

    @Test
    @DisplayName("Test when delete trip successfully")
    @Order(6)
    public void deleteTripTest2() {
        Destination destination = new Destination();
        destination.setAddress("adress");
        destination.setLatitude(300000.2);
        destination.setLongitude(2308.89989);

        Trip newTrip = new Trip();
        newTrip.setName("Trip test");
        newTrip.setThumbnailUrl("/trip");
        newTrip.setDestination(destination);
        newTrip.setDescription("memo");
        newTrip.setStatus(TripStatus.PUBLIC);
        tripRepository.save(newTrip);

        //Make sure the trip was saved
        Assert.notNull(newTrip.getId());
        Assert.notNull(destination.getId());

        tripRepository.deleteById(newTrip.getId());
        Optional<Trip> tripSaved = tripRepository.findById(newTrip.getId());
        assertFalse(tripSaved.isPresent());
    }

    @Test
    @DisplayName("Test when delete trip with the id is null")
    @Order(7)
    public void deleteTripTest3() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            tripRepository.deleteById(null);
        });
    }


}