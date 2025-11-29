package com.example.bulkupload.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString @AllArgsConstructor
public class SmaLeadSecond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String contactPerson;

    private String mobile;

    private String mobileSecond;

    private String email;

    private String address;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private String panNumber;

    private String gstNumber;

    private String businessCategory;

    private String personCategory;

}
