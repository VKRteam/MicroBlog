package com.whoslast.controllers;

import com.whoslast.entities.User;
import com.whoslast.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

	@Autowired
	UserRepository userRepository;  //Service which will do all data retrieval/manipulation work

	
	//-------------------Retrieve All Users--------------------------------------------------------
	
	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<Iterable<User>> listAllUsers() {
		Iterable<User> iterable = userRepository.findAll();
		/*
		List<User> list = new ArrayList<>();
		iterable.forEach(list::add);
		*/

		return new ResponseEntity<>(iterable, HttpStatus.OK);
	}


	//-------------------Retrieve list of Users by id--------------------------------------------------------


	@RequestMapping(value = "/user/getbyids/", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> listUsersById(@RequestBody List<Long> usersId) {
		User user;
		List<User> list = new ArrayList<>();

		for(long el: usersId){
			user = userRepository.findOne(el);
			if(user != null) {
				user.setLogin(null);
				user.setPassword(null);
				list.add(user);
			}
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}


	//-------------------Retrieve Single User--------------------------------------------------------
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {
		System.out.println("Fetching User with id " + id);
		User user = userRepository.findOne(id);
		if (user == null) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.setPassword(null);
		user.setLogin(null);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/userauth/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUserAuth(@PathVariable("login") String login) {
		System.out.println("Fetching User with login " + login);
		User user = userRepository.findByLogin(login);
		if (user == null) {
			System.out.println("User with login " + login + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		user.setFirstName(null);
		user.setLastName(null);
		user.setIconId(null);
		user.setEmail(null);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	
	
	//-------------------Create a User--------------------------------------------------------
	
	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " + user.getLogin());

		if (userRepository.existsByLogin(user.getLogin())) {
			System.out.println("A User with name " + user.getLogin() + " already exist");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		userRepository.save(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	
	//------------------- Update a User --------------------------------------------------------
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		System.out.println("Updating User " + id);
		
		User currentUser = userRepository.findOne(id);
		
		if (currentUser==null) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		currentUser.setLogin(user.getLogin());
		currentUser.setEmail(user.getEmail());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setIconId(user.getIconId());
        currentUser.setLastName(user.getLastName());

		userRepository.save(currentUser);
		return new ResponseEntity<>(currentUser, HttpStatus.OK);
	}

	//------------------- Delete a User --------------------------------------------------------
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
		System.out.println("Fetching & Deleting User with id " + id);

		User user = userRepository.findOne(id);
		if (user == null) {
			System.out.println("Unable to delete. User with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		userRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	//------------------- Delete All Users --------------------------------------------------------
	
	@RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		System.out.println("Deleting All Users");

		userRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
