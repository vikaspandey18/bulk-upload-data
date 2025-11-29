package com.example.bulkupload.controller;

import com.example.bulkupload.dto.ResponseDto;
import com.example.bulkupload.services.SmaLeadSecondService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class SmaLeadController {

    private SmaLeadSecondService smaLeadSecondService;

    @PostMapping(value = "/createSmaLead",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> createSmaLead(@RequestParam("file") MultipartFile file){
        boolean isCreated = smaLeadSecondService.createSmaLeadSecond(file);
        if(isCreated){
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("201","Created Successfully"));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("404","Failed to Import"));
        }

    }
}





















