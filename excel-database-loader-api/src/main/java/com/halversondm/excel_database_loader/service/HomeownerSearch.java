package com.halversondm.excel_database_loader.service;

import com.halversondm.excel_database_loader.dao.HomeownerDao;
import com.halversondm.excel_database_loader.data.Homeowner;

import java.util.ArrayList;
import java.util.List;

public class HomeownerSearch {

    private HomeownerDao homeownerDao;

    public HomeownerSearch(HomeownerDao homeownerDao) {
        this.homeownerDao = homeownerDao;
    }

    public List<Homeowner> searchHomeowners(String searchString) {
        List<Homeowner> searched = new ArrayList<>();
        if (searchString == null || searchString.isEmpty()) {
            return searched;
        }

        // Search by last name or address
        List<Homeowner> homeownersByAddress = homeownerDao.findHomeownersByAddressContainingIgnoreCase(searchString);
        List<Homeowner> homeownersByLastName = homeownerDao.findHomeownersByLastNameContainingIgnoreCase(searchString);
        if (homeownersByAddress != null) {
            searched.addAll(homeownersByAddress);
        }
        if (homeownersByLastName != null) {
            searched.addAll(homeownersByLastName);
        }
        return searched;
    }
}
