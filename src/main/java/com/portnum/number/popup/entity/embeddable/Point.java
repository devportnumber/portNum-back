package com.portnum.number.popup.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Point {

    private Float longitude;
    private Float latitude;
}
