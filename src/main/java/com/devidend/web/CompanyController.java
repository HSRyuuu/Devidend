package com.devidend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword){
        return null;
    }

    @GetMapping()
    public ResponseEntity<?> getCompanyList(){
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> addCompany(){
        return null;
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteCompany(@RequestParam String keyword){
        return null;
    }
}
