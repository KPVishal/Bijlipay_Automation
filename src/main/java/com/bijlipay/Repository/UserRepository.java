package com.bijlipay.Repository;

import com.bijlipay.Model.Role;
import com.bijlipay.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM [auto_users] u WHERE u.email =? AND u.user_pass =?",
            nativeQuery = true)
    List<User> findByUserNameAndPassword(String email, String pass);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Modifying
    @Query(value = "UPDATE [auto_users] SET isActive =?2 WHERE id IN ?1", nativeQuery =
            true)
    void deleteUserWithIds(List<Long> idList, boolean isActive);

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM [auto_users] u WHERE u.username =?", nativeQuery = true)
    User getUserDetails(String loginUser);

    @Query(value = "SELECT * FROM [auto_users] u WHERE u.username =?", nativeQuery = true)
    User findUserByUsername(String username);

    @Query(value = "SELECT u.username FROM [auto_users] u WHERE u.username =?", nativeQuery = true)
    String findusername(String username);

    @Query(value = "SELECT username FROM [auto_users] WHERE username LIKE ?1% ",
            nativeQuery = true)
    List<String> findAllUserName(String string);

    @Query(value = " SELECT  * FROM [auto_users] WHERE username !='opsadmin' AND " +
            "is_active= 'True' ", nativeQuery = true)
    List<User> getusers();

    @Query(value = "SELECT  ur.user_id FROM [auto_users_roles] ur WHERE role_id = ?1",
            nativeQuery =
                    true)
    List<Long> findAllUserBasedOnRole(Role result);

    @Query(value = "SELECT  * FROM [auto_users] WHERE user_id IN (?1)", nativeQuery =
            true)
    List<User> getAllUsersBasedOnUserIds(List<Long> usersIds);
    
    @Transactional
    @Modifying
    @Query(value="update d set d.mdr=b.mdr ,d.settlement_amount=b.netamount from bijlipay.dbo.ace_merchant_details d join Authentication.dbo.auto_bijlipay_mis_temp$ b on d.rrn=b.rrn where d.terminal_id=b.tid",nativeQuery=true)
 	int updateace();
}


