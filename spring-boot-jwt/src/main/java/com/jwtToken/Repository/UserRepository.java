package com.jwtToken.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwtToken.model.DAOUser;

public interface UserRepository extends JpaRepository<DAOUser, Integer> {

	DAOUser findByUsername(String username);

}
