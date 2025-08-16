package com.halversondm.excel_database_loader.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "homeowner")
@Entity
public class Homeowner {
    @Id
    private Integer lotNumber;
    private String closeDate;
    private String firstName;
    private String secondName;
    private String lastName;
    private String address;
    private String alternateAddress;
    private String city;
    private String state;
    private String zip;
}
