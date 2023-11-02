package com.skydan.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<AppUser> getAppUsers() {
        return appUserService.getAllAppUsers();
    }

    @GetMapping("{appUserId}")
    public AppUser getAppUser(@PathVariable("appUserId") Integer appUserId) {
        return appUserService.getAppUsersById(appUserId);
    }

    @PostMapping
    public void registerAppUser(@RequestBody AppUserRegistrationRequest request) {
        appUserService.addAppUser(request);
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
