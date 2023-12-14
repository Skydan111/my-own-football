package com.skydan.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppUserDataAccessService implements AppUserDao{

    private final AppUserRepository appUserRepository;

    public AppUserDataAccessService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<AppUser> selectAllAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public Optional<AppUser> selectAppUsersById(Integer id) {
        return appUserRepository.findById(id);
    }

    @Override
    public void insertAppUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public boolean existsUserWithEmail(String email) {
       return appUserRepository.existsAppUserByEmail(email);
    }

    @Override
    public boolean existsUserWithId(Integer appUserId) {
        return appUserRepository.existsAppUserById(appUserId);
    }

    @Override
    public void deleteAppUserById(Integer appUserId) {
        appUserRepository.deleteById(appUserId);
    }

    @Override
    public void updateAppUser(AppUser update) {
        appUserRepository.save(update);
    }

    @Override
    public Optional<AppUser> selectAppUserByEmail(String email) {
        return appUserRepository.findAppUserByEmail(email);
    }
}
