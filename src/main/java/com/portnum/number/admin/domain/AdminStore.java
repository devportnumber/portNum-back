package com.portnum.number.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({AuditingEntityListener.class})
@Table(name = "store")
public class AdminStore {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(name = "stat", nullable = true)
    private String stat;

    @Column(name = "neighborhood", nullable = true)
    private String neighborhood;

    @Column(name = "longitude", nullable = true)
    private String longitude;

    @Column(name = "latitude", nullable = true)
    private String latitude;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "address_detail", nullable = true)
    private String addressDetail;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "map_url", nullable = true)
    private String mapUrl;

    @Column(name = "start_time", nullable = true)
    private String startTime;

    @Column(name = "end_time", nullable = true)
    private String endTime;

    @Column(name = "valid", nullable = true)
    private String valid;

    public AdminStore(String name, String category, String startDate, String endDate, String stat) {
        this.name = name;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.stat = stat;
    }

    public AdminStore(AdminStore adminStore) {
        this.name = adminStore.getName();
        this.category = adminStore.getCategory();
        this.startDate = adminStore.getStartDate();
        this.endDate = adminStore.getEndDate();
        this.stat = adminStore.getStat();
    }
}
