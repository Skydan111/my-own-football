package com.skydan.user;

import com.skydan.exception.DuplicateResourceException;
import com.skydan.exception.RequestValidationException;
import com.skydan.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserDao appUserDao;
    private final AppUserDTOMapper appUserDTOMapper;
    private final PasswordEncoder passwordEncoder;


    public AppUserService(AppUserDao appUserDao, AppUserDTOMapper appUserDTOMapper, PasswordEncoder passwordEncoder) {
        this.appUserDao = appUserDao;
        this.appUserDTOMapper = appUserDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUserDTO> getAllAppUsers() {
        return appUserDao.selectAllAppUsers()
                .stream()
                .map(appUserDTOMapper)
                .collect(Collectors.toList());
    }

    public AppUserDTO getAppUsersById(Integer appUserId) {
        return appUserDao.selectAppUsersById(appUserId)
                .map(appUserDTOMapper)
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
                passwordEncoder.encode(appUserRegistrationRequest.password()),
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
        AppUser appUser = appUserDao.selectAppUsersById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException("appUser with id [%s] not found".formatted(appUserId)));

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

        if (request.team() != null && !request.team().equals(appUser.getTeam())) {
            appUser.setTeam(request.team());
            changes = true;
        }

        if (!changes) throw new RequestValidationException("no data changes found");

        appUserDao.updateAppUser(appUser);
    }
}
