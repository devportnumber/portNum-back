package com.portnum.number.admin.controller;

import com.portnum.number.admin.domain.AdminStore;
import com.portnum.number.admin.service.AdminListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
    public void deleteById(@RequestBody Integer id) {
        adminListService.deleteById(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody AdminStore adminStore){
        adminListService.save(adminStore);
    }

    @PostMapping("/update")
    public void update(@RequestBody AdminStore adminStore) {
        adminListService.update(adminStore.getStoreId(), adminStore);
    }


}
