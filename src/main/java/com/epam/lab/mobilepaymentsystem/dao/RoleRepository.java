package com.epam.lab.mobilepaymentsystem.dao;

import com.epam.lab.mobilepaymentsystem.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
