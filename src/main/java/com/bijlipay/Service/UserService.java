package com.bijlipay.Service;

import com.bijlipay.Model.ReportLog;
import com.bijlipay.Model.Role;
import com.bijlipay.Model.User;

import java.util.List;

public interface UserService {

		
	List<User> getusers();

	void delete(long id);

	User update(long id, User user);

	User findById(long userID);

	void deleteUsersWithIds(List<Long> idList, boolean isActive);

	User findByUserName(String username);

	List<ReportLog> getUsersWithDownload();


	List<Long> getAllUersBasedOnRole(Role result);

	List<User> getUsersBasedOnUserIds(List<Long> usersIds);
}
