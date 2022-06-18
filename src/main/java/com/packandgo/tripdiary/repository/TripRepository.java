package com.packandgo.tripdiary.repository;

import com.packandgo.tripdiary.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TripRepository extends PagingAndSortingRepository<Trip, Long> {
    Page<Trip> findAll(Pageable pageable);

    @Query("SELECT t FROM Trip t WHERE t.owner.id = ?1")
    List<Trip> findByUserId(Long id);
    @Query(value = "SELECT * FROM trip t WHERE t.begin_date = current_date() + t.notify_before", nativeQuery = true)
    List<Trip> getTripsForToday();

    boolean existsById(Long tripId);
}
