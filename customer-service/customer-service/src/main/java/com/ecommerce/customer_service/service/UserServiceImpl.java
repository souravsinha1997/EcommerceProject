package com.ecommerce.customer_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.customer_service.dto.LoginRequest;
import com.ecommerce.customer_service.dto.UserRequest;
import com.ecommerce.customer_service.dto.UserResponse;
import com.ecommerce.customer_service.entity.Address;
import com.ecommerce.customer_service.entity.Token;
import com.ecommerce.customer_service.entity.User;
import com.ecommerce.customer_service.exception.AddressNotFoundException;
import com.ecommerce.customer_service.exception.InvalidLoginException;
import com.ecommerce.customer_service.exception.UserNotFoundException;
import com.ecommerce.customer_service.repository.AddressRepository;
import com.ecommerce.customer_service.repository.UserRepository;
import com.ecommerce.customer_service.security.JwtService;
import com.ecommerce.customer_service.security.ValidateRequest;


@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final AddressRepository addressRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final ValidateRequest validate;
	    
	public UserServiceImpl(ValidateRequest validate,UserRepository userRepo,AddressRepository addressRepo,PasswordEncoder passwordEncoder,JwtService jwtService) {
		this.userRepo = userRepo;
		this.addressRepo = addressRepo;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.validate = validate;
	}
	
	public List<UserResponse> getAllUsers(){
		List<User> users =  userRepo.findAll();
		List<UserResponse> response = new ArrayList<>();
		for(User user : users) {
			UserResponse userResponse = new UserResponse();
			userResponse.setAddress(user.getAddress());
			userResponse.setEmail(user.getEmail());
			userResponse.setFirstName(user.getFirstName());
			userResponse.setLastName(user.getLastName());
			userResponse.setPhnNo(user.getPhnNo());
			userResponse.setUserName(user.getUsername());
			userResponse.setRole(user.getRole());
			response.add(userResponse);
		}
		return response;
	}
	
	public UserResponse getUser(int id){
		Optional<User> user =  userRepo.findById(id);
		UserResponse userResponse = new UserResponse();
		if(user==null) {
			throw new UserNotFoundException("User does not exist with id : "+id);
		}
		else {
			userResponse.setAddress(user.get().getAddress());
			userResponse.setEmail(user.get().getEmail());
			userResponse.setFirstName(user.get().getFirstName());
			userResponse.setLastName(user.get().getLastName());
			userResponse.setPhnNo(user.get().getPhnNo());
			userResponse.setUserName(user.get().getUsername());
			userResponse.setRole(user.get().getRole());
		}
		return userResponse;
	}
	
	@Transactional
	public String deleteUser(int id) {
		Optional<User> user = userRepo.findById(id);
		Optional<Address> address = addressRepo.findById(user.get().getAddress().getId());
		
		if(address!= null && user!=null) {
			userRepo.delete(user.get());
			addressRepo.delete(address.get());
		}
		else {
			throw new UserNotFoundException("User not found with id : "+id);
		}
		
		return "User account is removed successfully";
	}
	
	public UserResponse updateUser(UserRequest request, int id) {
		User savedUser = userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User not found with id : "+id));
		Address savedAddress = addressRepo.findById(savedUser.getAddress().getId()).orElseThrow(()->new AddressNotFoundException("Address not found for the user with id :"+id));
		
		if(request.getAddressLine()!=null && !request.getAddressLine().equals("")) {
			savedAddress.setAddressLine(request.getAddressLine());
		}
		if(request.getPin()!=null && !request.getPin().equals("")) {
			savedAddress.setPin(request.getPin());
		}
		
		Address newAddress = addressRepo.save(savedAddress);
		
		savedUser.setAddress(newAddress);
		if(request.getFirstName()!=null && !request.getFirstName().equals("")) {
			savedUser.setFirstName(request.getFirstName());
		}
		if(request.getLastName()!=null && !request.getLastName().equals("")) {
			savedUser.setLastName(request.getLastName());
		}
		if(request.getUserName()!=null && !request.getUserName().equals("")) {
			savedUser.setUserName(request.getUserName());
		}
		if(request.getPassword()!=null && !request.getPassword().equals("")) {
			savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		if(request.getPhnNo()!=null && !request.getPhnNo().equals("")) {
			savedUser.setPhnNo(request.getPhnNo());
		}
		if(request.getEmail()!=null && !request.getEmail().equals("")) {
			savedUser.setEmail(request.getEmail());
		}
		
		User newUser = userRepo.save(savedUser);
		
		UserResponse response = new UserResponse();
		response.setAddress(newUser.getAddress());
		response.setEmail(newUser.getEmail());
		response.setFirstName(newUser.getFirstName());
		response.setLastName(newUser.getLastName());
		response.setPhnNo(newUser.getPhnNo());
		response.setRole(newUser.getRole());
		response.setUserName(newUser.getUsername());
		
		return response;
		
	}
	
	@Transactional
	public String saveUser(UserRequest userRequest) {
		
		Address addr = new Address();
		addr.setAddressLine(userRequest.getAddressLine());
		addr.setPin(userRequest.getPin());
		Address savedAddress = addressRepo.save(addr);
		
		User user = new User();
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setUserName(userRequest.getUserName());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setPhnNo(userRequest.getPhnNo());
		user.setEmail(userRequest.getEmail());
		user.setRole(userRequest.getRole());
		user.setAddress(savedAddress);
		User savedUser = userRepo.save(user);
		
		
		return "User registered successfully";
	}
	
    public Token loginUser(LoginRequest loginRequest) {
        User user = userRepo.findByUserName(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidLoginException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidLoginException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);
        return new Token(token);
    }
	
}
