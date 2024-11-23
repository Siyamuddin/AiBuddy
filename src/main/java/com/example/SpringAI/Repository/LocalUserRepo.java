package com.example.SpringAI.Repository;

import com.example.SpringAI.Model.LocalUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalUserRepo extends JpaRepository<LocalUser,Long> {
    Page<LocalUser> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    List<LocalUser> findByEmailContainingIgnoreCase(String email);
    LocalUser findByEmail(String email);
}
