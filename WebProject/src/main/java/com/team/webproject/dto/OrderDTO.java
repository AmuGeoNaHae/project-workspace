package com.team.webproject.dto;

import lombok.Data;

@Data
public class OrderDTO {
	
	String advance_ticket_code;
	String booker_code;
	String performance_code;
	String booking_date;
	String booking_time;	
	int total_price;
	int adult_price;
	int adult_qty;
	int youth_price;
	int youth_qty;
	int child_price;
	int child_qty;
	
}
