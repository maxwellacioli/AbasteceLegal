package com.maxwell.AbasteceLegal.repository;


import com.maxwell.AbasteceLegal.model.Role;
import com.maxwell.AbasteceLegal.util.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}