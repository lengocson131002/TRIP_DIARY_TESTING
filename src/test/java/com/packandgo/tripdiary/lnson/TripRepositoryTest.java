package com.packandgo.tripdiary.lnson;

import com.packandgo.tripdiary.enums.TripStatus;
import com.packandgo.tripdiary.model.Destination;
import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.repository.TripRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    public void saveTripTest() {
        Trip trip = new Trip();
        trip.setName("Trip to HCM city");

        Destination destination = new Destination();
        destination.setAddress("Thu Duc Ho Chi Minh city");
        destination.setLatitude(10);
        destination.setLatitude(20);

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

//    @Test
//    public void updateTripTest() {
//
//    }
//
//    @Test
//    public void findByIdTest() {
//
//    }
//
//    @Test
//    public void searchTripTest() {
//
//    }
//
//    @Test
//    public void deleteByIdTest(){
//
//    }

}
