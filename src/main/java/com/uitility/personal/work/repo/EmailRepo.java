package com.uitility.personal.work.repo;

import com.uitility.personal.work.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepo extends JpaRepository<EmailEntity, Long> {
}
