package com.skydan.user;

import com.skydan.exception.DuplicateResourceException;
import com.skydan.exception.RequestValidationException;
import com.skydan.exception.ResourceNotFoundException;
import com.skydan.player.PlayerDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.skydan.user.Team.DYNAMO;
import static com.skydan.user.Team.SHAKHTAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserDao appUserDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private AppUserService underTest;
    private final AppUserDTOMapper appUserDTOMapper = new AppUserDTOMapper();
    private final PlayerDTOMapper playerDTOMapper = new PlayerDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new AppUserService(appUserDao, appUserDTOMapper, passwordEncoder, playerDTOMapper);
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
        AppUser appUser = new AppUser(id,"Foo", "foo@email.com", "password", SHAKHTAR);
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        AppUserDTO expected = appUserDTOMapper.apply(appUser);

        //When
        AppUserDTO actual = underTest.getAppUsersById(id);

        //Then
        assertThat(actual).isEqualTo(expected);
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
                "Foo", email, "password", SHAKHTAR);

        String passwordHash = "$%^%^UGvsdjnvGFNJM%^&R$";

        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);

        //When
        underTest.addAppUser(request);

        //Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserDao).insertAppUser(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getId()).isNull();
        assertThat(capturedAppUser.getName()).isEqualTo(request.name());
        assertThat(capturedAppUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedAppUser.getPassword()).isEqualTo(passwordHash);
    }

    @Test
    void willThrowWhenEmailExistWhileAddingAppUser() {
        //Given
        String email = "foo@email.com";
        when(appUserDao.existsUserWithEmail(email)).thenReturn(true);
        AppUserRegistrationRequest request = new AppUserRegistrationRequest(
                "Foo", email, "password", SHAKHTAR);

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
                id, "Foo", "foo@gmail.com", "password", SHAKHTAR
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        String newEmail = "bar@email.com";

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                "Bar", newEmail, DYNAMO);

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
        assertThat(capturedAppUser.getTeam()).isEqualTo(updateRequest.team());
    }

    @Test
    void canUpdateOnlyAppUserName() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", "password", SHAKHTAR
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
        assertThat(capturedAppUser.getEmail()).isEqualTo(appUser.getEmail());
        assertThat(capturedAppUser.getTeam()).isEqualTo(appUser.getTeam());
    }

    @Test
    void canUpdateOnlyAppUserEmail() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", "password", SHAKHTAR
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
        assertThat(capturedAppUser.getEmail()).isEqualTo(newEmail);
        assertThat(capturedAppUser.getTeam()).isEqualTo(appUser.getTeam());
    }

    @Test
    void canUpdateOnlyAppUserTeam() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", "password", SHAKHTAR
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                null, null, DYNAMO);

        // When
        underTest.updateAppUser(id, updateRequest);

        // Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(appUserDao).updateAppUser(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getName()).isEqualTo(appUser.getName());
        assertThat(capturedAppUser.getEmail()).isEqualTo(appUser.getEmail());
        assertThat(capturedAppUser.getTeam()).isEqualTo(updateRequest.team());
    }

    @Test
    void willThrowWhenTryingToUpdateAppUserEmailWhenAlreadyTaken() {
        // Given
        int id = 1;
        AppUser appUser = new AppUser(
                id, "Foo", "foo@email.com", "password", SHAKHTAR
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
                id, "Foo", "foo@email.com", "password", SHAKHTAR
        );
        when(appUserDao.selectAppUsersById(id)).thenReturn(Optional.of(appUser));

        AppUserUpdateRequest updateRequest = new AppUserUpdateRequest(
                appUser.getName(), appUser.getEmail(), appUser.getTeam());

        // When
        assertThatThrownBy(() -> underTest.updateAppUser(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then
        verify(appUserDao, never()).updateAppUser(any());
    }
}