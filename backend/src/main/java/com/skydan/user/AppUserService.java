package com.skydan.user;

import com.skydan.exception.DuplicateResourceException;
import com.skydan.exception.RequestValidationException;
import com.skydan.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;

    public AppUserService(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
    }

    public List<AppUser> getAllAppUsers() {
        return appUserDao.selectAllAppUsers();
    }

    public AppUser getAppUsersById(Integer appUserId) {
        return appUserDao.selectAppUsersById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("appUser with id [%s] not found".formatted(appUserId)));
    }

    public void addAppUser(AppUserRegistrationRequest appUserRegistrationRequest) {
        String email = appUserRegistrationRequest.email();
        if (appUserDao.existsUserWithEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }
        AppUser appUser = new AppUser(
                appUserRegistrationRequest.name(),
                appUserRegistrationRequest.email(),
                appUserRegistrationRequest.age(),
                appUserRegistrationRequest.team()
        );
        appUserDao.insertAppUser(appUser);
    }

    public void deleteAppUserById(Integer appUserId) {
        if (!appUserDao.existsUserWithId(appUserId)) {
            throw new ResourceNotFoundException("appUser with id [%s] not found".formatted(appUserId));
        }
        appUserDao.deleteAppUserById(appUserId);
    }

    public void updateAppUser(Integer appUserId, AppUserUpdateRequest request) {
        AppUser appUser = getAppUsersById(appUserId);

        boolean changes = false;

        if (request.name() != null && !request.name().equals(appUser.getName())) {
            appUser.setName(request.name());
            changes = true;
        }
        if (request.email() != null && !request.email().equals(appUser.getEmail())) {
            if (appUserDao.existsUserWithEmail(request.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            appUser.setEmail(request.email());
            changes = true;
        }
        if (request.age() != null && !request.age().equals(appUser.getAge())) {
            appUser.setAge(request.age());
            changes = true;
        }
        if (request.team() != null && !request.team().equals(appUser.getTeam())) {
            appUser.setTeam(request.team());
            changes = true;
        }
        if (!changes) throw new RequestValidationException("no data changes found");

        appUserDao.updateAppUser(appUser);
    }
}
