package com.portnum.number.popup.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Point {

    private String longitude;
    private String latitude;
}
