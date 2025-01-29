package org.dars.jsp_gram.repository;

import org.dars.jsp_gram.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	boolean existsByMobile(long mobile);

	boolean existsByUsername(String username);

	User findByUsername(String username);

}
