package com.halversondm.excel_database_loader.dao;

import com.halversondm.excel_database_loader.data.Homeowner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeownerDao extends JpaRepository<Homeowner, Integer> {

    List<Homeowner> findHomeownersByAddressContainingIgnoreCase(String address);

    List<Homeowner> findHomeownersByLastNameContainingIgnoreCase(String lastName);
}
