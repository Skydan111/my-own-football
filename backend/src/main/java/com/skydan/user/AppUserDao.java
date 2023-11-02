package com.skydan.user;

import java.util.List;
import java.util.Optional;

public interface AppUserDao {
    List<AppUser> selectAllAppUsers();
    Optional<AppUser> selectAppUsersById(Integer id);
    void insertAppUser(AppUser appUser);
    boolean existsUserWithEmail(String email);
    boolean existsUserWithId(Integer appUserId);
    void deleteAppUserById(Integer appUserId);
    void updateAppUser(AppUser update);
}
