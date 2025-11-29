package com.example.bulkupload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class SmaLeadSecondDto {
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
