package com.bijlipay.Controller;

import com.bijlipay.Model.ReportLog;
import com.bijlipay.Model.Role;
import com.bijlipay.Model.Scheduler;
import com.bijlipay.Model.User;
import com.bijlipay.Repository.ReportLogRepository;
import com.bijlipay.Repository.RoleRepository;
import com.bijlipay.Repository.SchedulerRepo;
import com.bijlipay.Security.CurrentUser;
import com.bijlipay.Security.UserPrincipal;
import com.bijlipay.Service.UserService;
import com.bijlipay.Utils.ApiResponse;
import com.bijlipay.Utils.DateUtil;
import org.jxls.template.SimpleExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.bijlipay.Utils.Constants.yyyyMMdd;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ReportLogRepository reportRepos;
    @Autowired
    private SchedulerRepo screpo;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        List<User> users = userService.getusers();
        logger.debug("users", users);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", users), HttpStatus.OK);
    }

    @GetMapping("/download/{username}/{from}/{to}")
    public ResponseEntity<?> getAllUsersWithDownload(@PathVariable("username") String username,
                                                     @PathVariable("from") long from, @PathVariable("to") long to, HttpServletResponse response)
            throws IOException {

        // List<ReportLog> users = userService.getUsersWithDownload();
        List<ReportLog> result = reportRepos.downloadReport(username, DateUtil.longToDate(from, yyyyMMdd),
                DateUtil.longToDate(to, yyyyMMdd));
        List<String> header = Arrays.asList("username", "description", "download_at");
        response.addHeader("content-disposition", "attachment; filename=userlog.xls");
        response.setContentType("application/vnd.ms-excel");
        if (result.size() > 0) {
            new SimpleExporter().gridExport(header, result, "username,description,download_at",
                    response.getOutputStream());
            response.flushBuffer();
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NO_CONTENT, "No Content"), HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = {"/logout"})
    public ResponseEntity<?> getmerchant(@CurrentUser UserPrincipal user) {
        Date now = new Date();
        Date updatedDate = new Date(now.getTime());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Scheduler scusername = screpo.findusername(user.getUsername());
        scusername.setIs_active(false);
        scusername.setExpired_At(updatedDate);
        screpo.save(scusername);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody User user, BindingResult bindingResult) {

        User userDetails = userService.findByUserName(user.getUsername());
        if (userDetails != null && userDetails.getId() != id) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "UserName is Already Exits"),
                    HttpStatus.CONFLICT);
        }
        User result = userService.update(id, user);

        if (bindingResult.hasErrors() || result == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Error"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Updated User " + id + "SuccessFully", result),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") long id)  {
        User deleteUser = userService.findById(id);
        if (deleteUser != null) {
            userService.delete(id);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Error"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success"), HttpStatus.OK);
    }


    @GetMapping("/getAllUsersBasedOnRole/{role}")
    public ResponseEntity<?> getAllUsersOnRole(@PathVariable("role") Role role) {
        Role result = roleRepository.findById(role.getRoleId()).get();
        if (result == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "role not found"),
                    HttpStatus.BAD_REQUEST);
        }
        List<Long> usersIds = userService.getAllUersBasedOnRole(result);
        List<User> users = userService.getUsersBasedOnUserIds(usersIds);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", users), HttpStatus.OK);
    }

}
