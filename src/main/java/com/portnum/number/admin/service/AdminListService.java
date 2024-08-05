package com.portnum.number.admin.service;

import com.portnum.number.admin.domain.AdminStore;
import com.portnum.number.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class AdminListService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public List<AdminStore> getAdminList(){
        return adminRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AdminStore> getAdminFilterList(String name, String category, String startDate, String endDate, String stat){
        //return adminRepository.findByNameOrCategoryOrStartDateOrEndDateOrStat(name, category, startDate, endDate, stat);
        return adminRepository.findByfilter(name, category, startDate, endDate, stat);
    }

//    @Transactional(readOnly = true)
//    public List<AdminStore> getAdminFilterList(String name, String category, String startDate, String endDate, String stat){
//        return adminRepository.findAll();
//    }

    @Transactional
    public void deleteById(int id){
         adminRepository.deleteById(id);
    }

    @Transactional
    public AdminStore save(AdminStore adminStore) {
        adminRepository.save(adminStore);
        return adminStore;
    }

    @Transactional
    public void update(Integer id, AdminStore updateStore) {
        AdminStore findStore = adminRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>(){
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("update 실패");
            }
        });
        if(updateStore.getName() != null) findStore.setName(updateStore.getName());
        if(updateStore.getCategory() != null) findStore.setCategory(updateStore.getCategory());
        if(updateStore.getStartDate() != null) findStore.setStartDate(updateStore.getStartDate());
        if(updateStore.getEndDate() != null) findStore.setEndDate(updateStore.getEndDate());
        if(updateStore.getStat() != null) findStore.setStat(updateStore.getStat());

        if(updateStore.getNeighborhood() != null) findStore.setNeighborhood(updateStore.getNeighborhood());
        if(updateStore.getLongitude() != null) findStore.setLongitude(updateStore.getLongitude());
        if(updateStore.getLatitude() != null) findStore.setLatitude(updateStore.getLatitude());
        if(updateStore.getAddress() != null) findStore.setAddress(updateStore.getAddress());
        if(updateStore.getAddressDetail() != null) findStore.setAddressDetail(updateStore.getAddressDetail());
        if(updateStore.getDescription() != null) findStore.setDescription(updateStore.getDescription());
        if(updateStore.getMapUrl() != null) findStore.setMapUrl(updateStore.getMapUrl());
        if(updateStore.getStartTime() != null) findStore.setStartTime(updateStore.getStartTime());
        if(updateStore.getEndTime() != null) findStore.setEndTime(updateStore.getEndTime());
        if(updateStore.getValid() != null) findStore.setValid(updateStore.getValid());
    }
}
