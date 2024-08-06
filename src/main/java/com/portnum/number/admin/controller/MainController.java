package com.portnum.number.admin.controller;

import com.portnum.number.admin.domain.AdminStore;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.admin.service.AdminListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * admin API
 *
 * @author 안지은
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    private final AdminListService adminListService;
    private final AdminRepository adminRepository;

    @GetMapping("/list")
    public ResponseEntity<?> doGetAsList() {
        return ResponseEntity.ok(adminListService.getAdminList());
    }

    @PostMapping("/list/filter")
    public ResponseEntity<?> doGetAsFilterList(@RequestBody AdminStore adminStore){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(adminListService.getAdminFilterList(adminStore.getName(), adminStore.getCategory(), adminStore.getStartDate(), adminStore.getEndDate(), adminStore.getStat()));
    }

    @PostMapping("/del")
    @Transactional
    public String deleteById(@RequestBody List<AdminStore> adminStore) {
        String result="";
        if(adminRepository.findById(adminStore.get(0).getStoreId()).isPresent()){
            for(int i =0; i<adminStore.size(); i++){
                adminRepository.deleteById(adminStore.get(i).getStoreId());
            }
            result = "success";
        }
        else result = "fail";
        return result;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody AdminStore adminStore){
        adminListService.save(adminStore);
        return new ResponseEntity<>("200", HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody AdminStore adminStore) {
        adminListService.update(adminStore.getStoreId(), adminStore);
        return new ResponseEntity<>("200", HttpStatus.OK);
    }


}
