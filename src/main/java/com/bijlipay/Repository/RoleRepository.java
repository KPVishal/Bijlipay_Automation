package com.bijlipay.Repository;

import com.bijlipay.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRole(String role);

    @Query(value = "SELECT role_id FROM [auto_users_roles] WHERE user_id=?1", nativeQuery =
            true)
    int findRoleByUserId(long id);

}
