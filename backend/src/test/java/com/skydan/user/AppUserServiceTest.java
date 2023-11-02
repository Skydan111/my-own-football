package com.skydan.user;

import com.skydan.exception.DuplicateResourceException;
import com.skydan.exception.RequestValidationException;
import com.skydan.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserDao appUserDao;
    private AppUserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AppUserService(appUserDao);
    }

    @Test
    void getAllAppUsers() {
        //When
        underTest.getAllAppUsers();

        //Then
        verify(appUserDao).selectAllAppUsers();
    }

    @Test
    void canGetAppUsersById() {
        //Given
        int id = 1;
        AppUser appUser = new AppUser(id,"Foo", "foo@email.com", 18);
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        //When
        AppUser actual = underTest.getAppUsersById(id);

        //Then
        assertThat(actual).isEqualTo(appUser);
    }

    @Test
    void willThrowWhenGetAppUsersByIdReturnEmptyOptional() {
        //Given
        int id = 1;
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getAppUsersById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("appUser with id [%s] not found".formatted(id));
    }

    @Test
    void addAppUser() {
        //Given
        String email = "foo@email.com";
        when(appUserDao.existsUserWithEmail(email)).thenReturn(false);
        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                "Foo", email, 18
        );

        //When
        underTest.addAppUser(request);

        //Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserDao).insertAppUser(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getId()).isNull();
        assertThat(capturedAppUser.getName()).isEqualTo(request.name());
        assertThat(capturedAppUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedAppUser.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistWhileAddingAppUser() {
        //Given
        String email = "foo@email.com";
        when(appUserDao.existsUserWithEmail(email)).thenReturn(true);
        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                "Foo", email, 18
        );

        //When
        assertThatThrownBy(() -> underTest.addAppUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        //Then
        verify(appUserDao, never()).insertAppUser(any());
    }

    @Test
    void deleteAppUserById() {
        //Given
        int id = 1;
        when(appUserDao.existsUserWithId(id)).thenReturn(true);

        //When
        underTest.deleteAppUserById(id);

        //Then
        verify(appUserDao).deleteAppUserById(id);
    }

    @Test
    void willThrowDeleteAppUserByIdNotExist() {
        //Given
        int id = 1;
        when(appUserDao.existsUserWithId(id)).thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.deleteAppUserById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("appUser with id [%s] not found".formatted(id));

        //Then
        verify(appUserDao, never()).deleteAppUserById(any());
    }

    @Test
    void canUpdateAllAppUserProperties() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@gmail.com", 19
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        String newEmail = "bar@email.com";

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                "Bar", newEmail, 23);

        when(appUserDao.existsUserWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateAppUser(id, updateRequest);

        // Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(appUserDao).updateAppUser(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedAppUser.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedAppUser.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", 19
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                "Bar", null, null);

        // When
        underTest.updateAppUser(id, updateRequest);

        // Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(appUserDao).updateAppUser(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedAppUser.getAge()).isEqualTo(appUser.getAge());
        assertThat(capturedAppUser.getEmail()).isEqualTo(appUser.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", 19
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        String newEmail = "bar@email.com";

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                null, newEmail, null);

        when(appUserDao.existsUserWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateAppUser(id, updateRequest);

        // Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(appUserDao).updateAppUser(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getName()).isEqualTo(appUser.getName());
        assertThat(capturedAppUser.getAge()).isEqualTo(appUser.getAge());
        assertThat(capturedAppUser.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", 19
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                null, null, 22);

        // When
        underTest.updateAppUser(id, updateRequest);

        // Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(appUserDao).updateAppUser(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getName()).isEqualTo(appUser.getName());
        assertThat(capturedAppUser.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedAppUser.getEmail()).isEqualTo(appUser.getEmail());
    }

    @Test
    void willThrowWhenTryingToUpdateAppUserEmailWhenAlreadyTaken() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", 19
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        String newEmail = "bar@email.com";

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                null, newEmail, null);

        when(appUserDao.existsUserWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateAppUser(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(appUserDao, never()).updateAppUser(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", 19
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                appUser.getName(), appUser.getEmail(), appUser.getAge());

        // When
        assertThatThrownBy(() -> underTest.updateAppUser(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then
        verify(appUserDao, never()).updateAppUser(any());
    }
}