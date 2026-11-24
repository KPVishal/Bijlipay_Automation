package com.bijlipay.Controller;

import com.bijlipay.Model.DTO.ForgotPassword;
import com.bijlipay.Model.DTO.LoginRequest;
import com.bijlipay.Model.DTO.SignUpRequest;
import com.bijlipay.Model.Role;
import com.bijlipay.Model.Scheduler;
import com.bijlipay.Model.User;
import com.bijlipay.Repository.RoleRepository;
import com.bijlipay.Repository.SchedulerRepo;
import com.bijlipay.Repository.UserRepository;
import com.bijlipay.Security.JwtTokenProvider;
import com.bijlipay.Utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/authorization")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchedulerRepo screpo;

    @Autowired
    private JwtTokenProvider jwttokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    //    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        Date now = new Date();
        Scheduler scheduled = new Scheduler();
        String username = userRepository.findusername(loginRequest.getUsername());
        User userData = userRepository.findUserByUsername(username);
        if (!(username == null)) {
            if (passwordEncoder.matches(loginRequest.getPassword(), userData.getPassword())) {
                screpo.findUserActive(loginRequest.getUsername());
                scheduled = screpo.findusername(loginRequest.getUsername());
                if (!(scheduled.getIs_active())) {
                    String loginUser = loginRequest.getUsername();
                    User user = userRepository.getUserDetails(loginUser);
                    int roleId = roleRepository.findRoleByUserId(user.getId());
                    Role role = roleRepository.findById(roleId).get();
                    if (user.isActive()) {
                        Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                        loginRequest.getPassword()));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String access_token = jwttokenProvider.generateToken(authentication);
                        HashMap<String, Object> response = new HashMap<>();
                        response.put("access_token", access_token);
                        response.put("name", user.getName());
                        response.put("role", role.getRole());
                        scheduled.setIs_active(true);
                        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
                        scheduled.setExpired_At(expiryDate);
                        screpo.save(scheduled);
                        return ResponseEntity.ok(response);
                    }
                    return new ResponseEntity(new ApiResponse(HttpStatus.UNAUTHORIZED, "Provide Valid User"),
                            HttpStatus.UNAUTHORIZED);
                }
                return new ResponseEntity(
                        new ApiResponse(HttpStatus.BAD_REQUEST, "User already Logged in, try after 30 mins"),
                        HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "Bad credential"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.UNAUTHORIZED, "UserName is Not valid"),
                HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/signup")
    public ResponseEntity<?> userRegister(@Valid @RequestBody final SignUpRequest signUpRequest,
                                          BindingResult bindingResult,
                                          HttpServletRequest request) throws Exception {

        log.info("signUpRequest {}", signUpRequest);
        Date now = new Date();
        Scheduler scheduled = new Scheduler();
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "error"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(HttpStatus.CONFLICT, "UserName already taken!"),
                    HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(HttpStatus.CONFLICT, "Email_id already taken!"),
                    HttpStatus.CONFLICT);
        }

        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirm_password())) {
            return new ResponseEntity(
                    new ApiResponse(HttpStatus.BAD_REQUEST, "Password and Confirm Password not Matching"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
                signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByRole(signUpRequest.getRole());
        if (role == null) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "Role Not Found For This User"),
                    HttpStatus.BAD_REQUEST);
        }
        user.setRole(Arrays.asList(role));
        user.setActive(true);
        User result = userRepository.save(user);
        scheduled.setIs_active(false);
        scheduled.setUsername(user.getUsername());
        scheduled.setExpired_At(new Date(now.getTime()));
        screpo.save(scheduled);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED, "User registered successfully", result),
                HttpStatus.CREATED);
    }

    @SuppressWarnings({"unused", "unchecked", "rawtypes"})
    @PostMapping("/resetpassword")
    public ResponseEntity<?> forgotpassword(@RequestBody final ForgotPassword forgotPassword) throws Exception {

        log.info("forgotPassword {}", forgotPassword);
        String username = userRepository.findusername(forgotPassword.getUsername());

        if (!(null == username)) {
            User user = userRepository.findUserByUsername(forgotPassword.getUsername());
            boolean oldPass = passwordEncoder.matches(forgotPassword.getOldPass(), user.getPassword());
            if (!oldPass) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "old password not matched"),
                        HttpStatus.BAD_REQUEST);
            }
            if (!forgotPassword.getNewPass().equals(forgotPassword.getConfirmNewPass())) {
                return new ResponseEntity(
                        new ApiResponse(HttpStatus.BAD_REQUEST, "password and confirm password not matching"),
                        HttpStatus.BAD_REQUEST);
            } else {
                user.setPassword(passwordEncoder.encode(forgotPassword.getNewPass()));
                userRepository.save(user);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "password changed successfully"), HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, "user not founded"),
                HttpStatus.BAD_REQUEST);
    }
}
