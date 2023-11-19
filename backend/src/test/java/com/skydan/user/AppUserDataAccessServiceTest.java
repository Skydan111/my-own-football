package com.skydan.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class AppUserDataAccessServiceTest {

    private AppUserDataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserDataAccessService(appUserRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllAppUsers() {
        //When
        underTest.selectAllAppUsers();

        //Then
        verify(appUserRepository).findAll();
    }

    @Test
    void selectAppUsersById() {
        //Given
        int id = 1;

        //When
        underTest.selectAppUsersById(id);

        //Then
        verify(appUserRepository).findById(id);
    }

    @Test
    void insertAppUser() {
        //Given
        AppUser appUser = new AppUser(1, "Foo", "foo@email.com", 18, Team.SHAKHTAR);

        //When
        underTest.insertAppUser(appUser);

        //Then
        verify(appUserRepository).save(appUser);
    }

    @Test
    void existsUserWithEmail() {
        //Given
        String email = "foo@email.com";

        //When
        underTest.existsUserWithEmail(email);

        //Then
        verify(appUserRepository).existsAppUserByEmail(email);
    }

    @Test
    void existsUserWithId() {
        //Given
        int id = 1;

        //When
        underTest.existsUserWithId(id);

        //Then
        verify(appUserRepository).existsAppUserById(id);
    }

    @Test
    void deleteAppUserById() {
        //Given
        int id = 1;

        //When
        underTest.deleteAppUserById(id);

        //Then
        verify(appUserRepository).deleteById(id);
    }

    @Test
    void updateAppUser() {
        //Given
        AppUser appUser = new AppUser(1, "Foo", "foo@email.com", 18, Team.SHAKHTAR);

        //When
        underTest.updateAppUser(appUser);

        //Then
        verify(appUserRepository).save(appUser);
    }
}