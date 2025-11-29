package com.example.bulkupload.repository;

import com.example.bulkupload.entity.SmaLeadSecond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmaLeadSecondRepository  extends JpaRepository<SmaLeadSecond,Long> {
}
