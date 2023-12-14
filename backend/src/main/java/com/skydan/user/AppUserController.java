package com.skydan.user;

import com.skydan.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;
    private final JWTUtil jwtUtil;

    public AppUserController(AppUserService appUserService, JWTUtil jwtUtil) {
        this.appUserService = appUserService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<AppUserDTO> getAppUsers() {
        return appUserService.getAllAppUsers();
    }

    @GetMapping("{appUserId}")
    public AppUserDTO getAppUser(@PathVariable("appUserId") Integer appUserId) {
        return appUserService.getAppUsersById(appUserId);
    }

    @PostMapping
    public ResponseEntity<?> registerAppUser(@RequestBody AppUserRegistrationRequest request) {
        appUserService.addAppUser(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("{appUserId}")
    public void deleteAppUser(@PathVariable("appUserId") Integer appUserId){
        appUserService.deleteAppUserById(appUserId);
    }

    @PutMapping("{appUserId}")
    public void updateAppUser(@PathVariable("appUserId") Integer appUserId,
                              @RequestBody AppUserUpdateRequest request) {
        appUserService.updateAppUser(appUserId, request);
    }
}
