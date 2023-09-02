package com.devidend.web;

import com.devidend.model.Company;
import com.devidend.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword){
        return null;
    }

    @GetMapping()
    public ResponseEntity<?> getCompanyList(){
        return null;
    }

    @PostMapping()
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }
        Company company = companyService.save(ticker);

        return ResponseEntity.ok(company);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteCompany(@RequestParam String keyword){
        return null;
    }
}
