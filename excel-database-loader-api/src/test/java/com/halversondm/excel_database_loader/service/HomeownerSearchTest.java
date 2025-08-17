package com.halversondm.excel_database_loader.service;

import com.halversondm.excel_database_loader.dao.HomeownerDao;
import com.halversondm.excel_database_loader.data.Homeowner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeownerSearchTest {

    private HomeownerDao homeownerDao;
    private HomeownerSearch homeownerSearch;

    @BeforeEach
    void setUp() {
        homeownerDao = mock(HomeownerDao.class);
        homeownerSearch = new HomeownerSearch(homeownerDao);
    }

    @Test
    void testSearchHomeowners_nullOrEmpty() {
        assertTrue(homeownerSearch.searchHomeowners(null).isEmpty());
        assertTrue(homeownerSearch.searchHomeowners("").isEmpty());
    }

    @Test
    void testSearchHomeowners_byAddressAndLastName() {
        Homeowner h1 = new Homeowner();
        Homeowner h2 = new Homeowner();
        when(homeownerDao.findHomeownersByAddressContainingIgnoreCase("Smith")).thenReturn(List.of(h1));
        when(homeownerDao.findHomeownersByLastNameContainingIgnoreCase("Smith")).thenReturn(List.of(h2));

        List<Homeowner> result = homeownerSearch.searchHomeowners("Smith");
        assertEquals(2, result.size());
        assertTrue(result.contains(h1));
        assertTrue(result.contains(h2));
    }

    @Test
    void testSearchHomeowners_nullResults() {
        when(homeownerDao.findHomeownersByAddressContainingIgnoreCase("Test")).thenReturn(null);
        when(homeownerDao.findHomeownersByLastNameContainingIgnoreCase("Test")).thenReturn(null);

        List<Homeowner> result = homeownerSearch.searchHomeowners("Test");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchHomeowners_onlyAddress() {
        Homeowner h1 = new Homeowner();
        when(homeownerDao.findHomeownersByAddressContainingIgnoreCase("Main")).thenReturn(List.of(h1));
        when(homeownerDao.findHomeownersByLastNameContainingIgnoreCase("Main")).thenReturn(null);

        List<Homeowner> result = homeownerSearch.searchHomeowners("Main");
        assertEquals(1, result.size());
        assertTrue(result.contains(h1));
    }

    @Test
    void testSearchHomeowners_onlyLastName() {
        Homeowner h2 = new Homeowner();
        when(homeownerDao.findHomeownersByAddressContainingIgnoreCase("Doe")).thenReturn(null);
        when(homeownerDao.findHomeownersByLastNameContainingIgnoreCase("Doe")).thenReturn(List.of(h2));

        List<Homeowner> result = homeownerSearch.searchHomeowners("Doe");
        assertEquals(1, result.size());
        assertTrue(result.contains(h2));
    }
}

