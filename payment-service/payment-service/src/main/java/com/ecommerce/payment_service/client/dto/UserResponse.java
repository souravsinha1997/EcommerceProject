package com.ecommerce.payment_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserResponse {

	private String firstName;
	private String lastName;
	private String userName;
	private String phnNo;
	private String email;
	private Role role;
	private Address address;

	
}
