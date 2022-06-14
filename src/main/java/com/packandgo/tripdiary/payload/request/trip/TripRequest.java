package com.packandgo.tripdiary.payload.request.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.packandgo.tripdiary.enums.TripStatus;
import com.packandgo.tripdiary.model.Destination;
import com.packandgo.tripdiary.model.PriceItem;
import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.VisitDay;
import com.packandgo.tripdiary.payload.TripPayload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripRequest extends TripPayload {
    public TripRequest() {
        super();
    }

}
