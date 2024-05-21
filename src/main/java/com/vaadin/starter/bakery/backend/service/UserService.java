package com.vaadin.starter.bakery.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

@Service
public class UserService {

	public static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";
	private static final String DELETING_SELF_NOT_PERMITTED = "You cannot delete your own account";
	
	private final UserRepository userRepository;


	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	 public List<User> findAllUsers(String stringFilter) {
		   if (stringFilter == null || stringFilter.isEmpty()) { 
			   return userRepository.findAll();
	        } else {
	        	//System.out.println("IN Filter..........");
	            return userRepository.search(stringFilter);
	        }
	            
	  }


	private void throwIfDeletingSelf(User currentUser, User user) {
		if (currentUser.equals(user)) {
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	private void throwIfUserLocked(User entity) {
		if (entity != null && entity.isLocked()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}
	
    public void deleteUser(User user) {
    	userRepository.delete(user);
    }

    
	public void saveUser(User user) {
        if (user == null) { 
            System.err.println("User is null. Are you sure you have connected your form to the application?");
            return;
        }
        userRepository.save(user);
    }

}
