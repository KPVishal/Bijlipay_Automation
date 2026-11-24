package com.bijlipay.Controller;

import com.bijlipay.Model.Role;
import com.bijlipay.Repository.RoleRepository;
import com.bijlipay.Utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/getallroles")
    public ResponseEntity<?> getAllUsers() throws URISyntaxException {
        List<Role> roleList = roleRepository.findAll();
        logger.debug("roles {}", roleList);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", roleList), HttpStatus.OK);
    }
}
