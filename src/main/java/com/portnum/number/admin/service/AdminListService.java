package com.portnum.number.admin.service;

import com.portnum.number.admin.domain.AdminStore;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.common.domain.enums.Valid;
import com.portnum.number.store.repository.StoreRepository;
import com.portnum.number.store.response.StoreListResponse;
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
}
