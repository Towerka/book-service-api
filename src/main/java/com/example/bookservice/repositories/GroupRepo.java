package com.example.bookservice.repositories;

import com.example.bookservice.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository(value = "groups")
public interface GroupRepo extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    List<Group> getGroupsByName(String name);

    List<Group> findByNameContaining(String name);

    @Transactional
    void deleteGroupById(Long id);

}
