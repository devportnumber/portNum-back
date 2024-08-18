package com.portnum.number.popup.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String address;
    private String addressDetail;
    private String region;
}
