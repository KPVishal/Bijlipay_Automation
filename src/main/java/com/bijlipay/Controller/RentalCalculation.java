package com.bijlipay.Controller;

import com.bijlipay.Service.RentService;
import com.bijlipay.Utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/api/rent")
public class RentalCalculation {

    @Autowired
    private RentService rentService;

    @GetMapping("/rentCalculation")
    private ResponseEntity rentalCalculation(){
        rentService.calculateRental();
        return new ResponseEntity(new ApiResponse(HttpStatus.OK, "File Import "),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{action}")
    public ResponseEntity importRent(@PathVariable("action") boolean action) throws IOException {
        rentService.importData(action);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
    }

}
