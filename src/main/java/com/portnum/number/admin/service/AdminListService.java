package com.portnum.number.admin.service;

import com.portnum.number.admin.domain.AdminStore;
import com.portnum.number.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminListService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public List<AdminStore> getAdminList(){
        return adminRepository.findAll();
    }

//    @Transactional(readOnly = true)
//    public AdminStore getAdminFilterList(String name){
//        return new AdminStore(getAdminName(name));
//        //return adminRepository.findByName(name);
//    }

    @Transactional(readOnly = true)
    public List<AdminStore> getAdminName(String name){
        return adminRepository.findByName(name);
    }
}
