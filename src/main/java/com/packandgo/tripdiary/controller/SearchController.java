package com.packandgo.tripdiary.controller;

import com.packandgo.tripdiary.payload.response.SearchResponse;
import com.packandgo.tripdiary.service.TripService;
import com.packandgo.tripdiary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/search")
public class SearchController {

    public final TripService tripService;
    public final UserService userService;

    public SearchController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> search(@RequestParam(name = "text", required = true) String keyword) {
        SearchResponse response= new SearchResponse();
        response.setKeyword(keyword);
        response.setTrips(tripService.search(keyword));
        response.setUsers(userService.search(keyword));
        return ResponseEntity.ok(response);
    }
}
