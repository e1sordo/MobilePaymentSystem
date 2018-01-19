package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {

}
