package com.quick_park_assist.entity;

import java.util.Date;

import ch.qos.logback.core.boolex.EvaluationException;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.quick_park_assist.enums.BookingSpotStatus;
import com.quick_park_assist.enums.PaymentMethod;

import lombok.Data;

@Entity
@Data
public class BookingSpot {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private Long bookingId;
	@Column(name = "userid")
	private Long userID;
	@Column(name = "spotid")
	private Long spotID;
	@Column(name = "mobile_number")
	private String mobileNumber;
	private Double duration;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date startTime;
	private Double estimatedPrice;
	@Enumerated(EnumType.STRING) // Stores enum as a String (e.g., "CREDIT_CARD")
	@Column(name = "payment_method", nullable = true)
	private PaymentMethod paymentMethod;
	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status")// Stores enum as a String (e.g., "BOOKED"
	private BookingSpotStatus bookingSpotStatus = BookingSpotStatus.CONFIRMED;

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Long getSpotID() {
		return spotID;
	}

	public void setSpotID(Long spotID) {
		this.spotID = spotID;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Double getEstimatedPrice() {
		return estimatedPrice;
	}

	public void setEstimatedPrice(Double estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BookingSpotStatus getBookingSpotStatus() {
		return bookingSpotStatus;
	}

	public void setBookingSpotStatus(BookingSpotStatus bookingSpotStatus) {
		this.bookingSpotStatus = bookingSpotStatus;
	}


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