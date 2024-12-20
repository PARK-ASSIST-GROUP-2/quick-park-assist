package com.quick_park_assist.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.quick_park_assist.enums.BookingSpotStatus;
import com.quick_park_assist.enums.PaymentMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BookingSpot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
	private Long userID;
	private Long spotID;
	private String mobileNumber;
	private Double duration;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date startTime;
	private Double estimatedPrice;
	@Enumerated
	private PaymentMethod paymentMethod;
	@Enumerated
	private BookingSpotStatus bookingSpotStatus;
}

//create table booking_spot (
//        booking_id bigint not null auto_increment,
//        booking_spot_status tinyint check (booking_spot_status between 0 and 3),
//        duration float(53),
//        estimated_price float(53),
//        mobile_number varchar(255),
//        payment_method tinyint check (payment_method between 0 and 2),
//        spotid bigint,
//        start_time datetime(6),
//        userid bigint,
//        primary key (booking_id)
//    )