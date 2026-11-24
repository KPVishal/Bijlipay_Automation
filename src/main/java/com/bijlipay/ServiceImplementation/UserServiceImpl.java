package com.bijlipay.ServiceImplementation;

import com.bijlipay.Exception.UserNotFoundException;
import com.bijlipay.Model.ReportLog;
import com.bijlipay.Model.Role;
import com.bijlipay.Model.User;
import com.bijlipay.Repository.ReportLogRepository;
import com.bijlipay.Repository.UserRepository;
import com.bijlipay.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ReportLogRepository reportLogRepository;

//	@Override
//	public List<User> users() {
//		// TODO Auto-generated method stub
//		return userRepository.findAll();
//	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(id).get();
		if (user != null) {
			user.setActive(false);
			userRepository.save(user);
		}
	}

	@Override
	public User update(long id, User user) {
		// TODO Auto-generated method stub
		User update = userRepository.findById(id).get();
		if (update != null) {
			update.setName(user.getName());
			update.setUsername(user.getUsername());
			update.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userRepository.save(update);
		}
		return update;
	}

	@Override
	public User findById(long userID) {
		// TODO Auto-generated method stub
		if (!userRepository.existsById(userID)) {
			throw new UserNotFoundException("UserID " + userID + " not found");

		}
		return userRepository.findById(userID).get();
	}

	@Override
	public void deleteUsersWithIds(List<Long> idList, boolean isActive) {
		// TODO Auto-generated method stub
		userRepository.deleteUserWithIds(idList, isActive);

	}

	@Override
	public User findByUserName(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username).get();
	}

	@Override
	public List<ReportLog> getUsersWithDownload() {
		// TODO Auto-generated method stub
		return reportLogRepository.findAllUserByOrder();
	}

	@Override
	public List<Long> getAllUersBasedOnRole(Role result) {
		return userRepository.findAllUserBasedOnRole(result);
	}

	@Override
	public List<User> getUsersBasedOnUserIds(List<Long> usersIds) {
		return userRepository.getAllUsersBasedOnUserIds(usersIds);
	}

	@Override
	public List<User> getusers() {
	
		return userRepository.getusers();
	}


}
