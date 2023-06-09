package ru.smartup.talksscanner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.smartup.talksscanner.criteria.SpecificationBuilder;
import ru.smartup.talksscanner.domain.Idea;
import ru.smartup.talksscanner.domain.IdeaStatus;
import ru.smartup.talksscanner.domain.User;
import ru.smartup.talksscanner.dto.responses.IdeaDtoResponse;
import ru.smartup.talksscanner.exception.ErrorCode;
import ru.smartup.talksscanner.exception.NotFoundEntityException;
import ru.smartup.talksscanner.exception.ServiceException;
import ru.smartup.talksscanner.repos.IdeaRepo;
import ru.smartup.talksscanner.repos.UserRepo;
import ru.smartup.talksscanner.tools.FilterMapper;
import ru.smartup.talksscanner.tools.IdeaMapper;
import ru.smartup.talksscanner.tools.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.smartup.talksscanner.test_data.IdeaTestData.*;
import static ru.smartup.talksscanner.test_data.UserTestData.user;



public class TestIdeaService {

    private IdeaService ideaService;
    private UserRepo userRepo;
    private IdeaRepo ideaRepo;
    private IdeaMapper ideaMapper;

    @BeforeEach
    public void init() {
        userRepo = Mockito.mock(UserRepo.class);
        ideaRepo = Mockito.mock(IdeaRepo.class);

        ideaMapper = new IdeaMapper();
        ideaService = new IdeaService(ideaRepo, new FilterMapper(), ideaMapper, new AuthUserService(userRepo, Mockito.mock(UserMapper.class)), new SpecificationBuilder());
    }

    @Test
    public void testInsertIdea() throws ServiceException {

        User user = user();
        Idea idea = idea(0, user);
        Idea ideaAfterSave = idea(1, user);

        Mockito.when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        Mockito.when(ideaRepo.save(idea)).thenReturn(ideaAfterSave);

        IdeaDtoResponse actual = ideaService.insertIdea(saveIdeaDtoRequest(), user.getLogin());
        IdeaDtoResponse expected = ideaMapper.convertToDto(ideaAfterSave);

        assertEquals(actual, expected);
    }

    @Test
    public void testInsertIdea__insertOneRequestTwice__byOneUser() throws ServiceException {
        User user = user();
        Idea idea = idea(0, user);
        Idea ideaAfterSaveFirst = idea(1, user);
        Idea ideaAfterSaveSecond = idea(2, user);

        Mockito.when(userRepo.findByLogin("login")).thenReturn(Optional.of(user));
        Mockito.when(ideaRepo.save(idea)).thenReturn(ideaAfterSaveFirst).thenReturn(ideaAfterSaveSecond);

        IdeaDtoResponse actualFirst = ideaService.insertIdea(saveIdeaDtoRequest(), user.getLogin());
        IdeaDtoResponse actualSecond = ideaService.insertIdea(saveIdeaDtoRequest(), user.getLogin());

        IdeaDtoResponse expectedFirst = ideaMapper.convertToDto(ideaAfterSaveFirst);
        IdeaDtoResponse expectedSecond = ideaMapper.convertToDto(ideaAfterSaveSecond);

        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }

    @Test
    public void testInsertIdea__insertOneRequestTwice__byOtherUsers() throws ServiceException {
        User userFirst = user();
        User userSecond = user(2);

        Idea idea = idea(0, userFirst);
        Idea ideaAfterSaveFirst = idea(1, userFirst);
        Idea ideaAfterSaveSecond = idea(2, userSecond);

        //Mock return Idea
        Mockito.when(ideaRepo.save(idea)).thenReturn(ideaAfterSaveFirst).thenReturn(ideaAfterSaveSecond);

        //Mock first User
        Mockito.when(userRepo.findByLogin(userFirst.getLogin())).thenReturn(Optional.of(userFirst));

        //Response first User
        IdeaDtoResponse actualFirst = ideaService.insertIdea(saveIdeaDtoRequest(), userFirst.getLogin());

        //Mock second User
        Mockito.when(userRepo.findByLogin(userSecond.getLogin())).thenReturn(Optional.of(userSecond));
        //Response second User
        IdeaDtoResponse actualSecond = ideaService.insertIdea(saveIdeaDtoRequest(), userSecond.getLogin());

        IdeaDtoResponse expectedFirst = ideaMapper.convertToDto(ideaAfterSaveFirst);
        IdeaDtoResponse expectedSecond = ideaMapper.convertToDto(ideaAfterSaveSecond);

        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }

    @Test
    public void testInsertIdea__userNotFoundByLogin() {
        Mockito.when(userRepo.findByLogin(Mockito.anyString())).thenReturn(Optional.empty());

        ServiceException actual = assertThrows(ServiceException.class, () -> ideaService.insertIdea(saveIdeaDtoRequest(), user(2).getLogin()));
        ServiceException expected = new ServiceException(ErrorCode.USER_NOT_FOUND, "");

        assertEquals(expected.getErrorCode(), actual.getErrorCode());
    }

    @Test
    public void testUpdateIdea() throws ServiceException {
        User user = user();
        Idea idea = idea(1, user);

        String initTitle = idea.getTitle();
        String initDescription = idea.getDescription();

        Mockito.when(userRepo.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        Mockito.when(ideaRepo.findByIdAndUserIdAndStatusNot(idea.getId(), user.getId(), IdeaStatus.DELETED)).thenReturn(Optional.of(idea));
        Mockito.when(ideaRepo.save(idea)).thenReturn(idea);

        IdeaDtoResponse response = ideaService.updateIdea(idea.getId(), saveIdeaDtoRequest("titleUpdate", "descUpdate"), user.getLogin());

        assertAll( () -> {
                    assertEquals(idea.getUser().getId(), response.getUser().getUserId());
                    assertEquals(idea.getDescription(), response.getDescription());
                    assertEquals(idea.getTitle(), response.getTitle());

                    assertNotEquals(initTitle, idea.getTitle());
                    assertNotEquals(initDescription, idea.getDescription());
                }
        );
    }

    @Test
    public void testUpdateIdea__userNotFoundByLogin() {
        User user = user();
        Idea idea = idea(1, user);

        Mockito.when(userRepo.findByLogin(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(ideaRepo.findById(idea.getId())).thenReturn(Optional.of(idea));

        ServiceException actual = assertThrows(ServiceException.class, () -> ideaService.updateIdea(1, saveIdeaDtoRequest(), user(2).getLogin()));
        ServiceException expected = new ServiceException(ErrorCode.USER_NOT_FOUND, "");

        assertEquals(expected.getErrorCode(), actual.getErrorCode());

    }

    @Test
    public void testUpdateIdea__userUpdateWrongIdea() {
        User owner = user();
        User other = user(2);

        Idea idea = idea(1, owner);

        Mockito.when(userRepo.findByLogin(other.getLogin())).thenReturn(Optional.of(other));
        Mockito.when(userRepo.findById(owner.getId())).thenReturn(Optional.of(owner));

        Mockito.when(ideaRepo.findByIdAndUserIdAndStatusNot( idea.getId(), other.getId(), IdeaStatus.DELETED)).thenReturn( Optional.empty());

        ServiceException actual = assertThrows(ServiceException.class, () -> ideaService.updateIdea(1, saveIdeaDtoRequest(), user(2).getLogin()));
        ServiceException expected = new ServiceException(ErrorCode.IDEA_NOT_FOUND, "");

        assertEquals(expected.getErrorCode(), actual.getErrorCode());
    }

    @Test
    public void testUpdateIdea__afterRemoving() throws NotFoundEntityException {
        User owner = user();

        Idea ideaBeforeDelete = idea(1, owner);
        Idea ideaAfterDelete = idea(ideaBeforeDelete.getId(), owner, IdeaStatus.DELETED);

        Mockito.when(userRepo.findByLogin(owner.getLogin())).thenReturn(Optional.of(owner));
        Mockito.when(ideaRepo.findByIdAndUserIdAndStatusNot( ideaBeforeDelete.getId(), owner.getId(), IdeaStatus.DELETED ))
                .thenReturn(Optional.of(ideaBeforeDelete))
                .thenReturn(Optional.empty());

        Mockito.when(ideaRepo.save(ideaAfterDelete)).thenReturn(ideaAfterDelete);

        IdeaDtoResponse deletedResponse = ideaService.removeIdea(ideaBeforeDelete.getId(), owner.getLogin());

        ServiceException actual = assertThrows(ServiceException.class, () -> ideaService.updateIdea(1, saveIdeaDtoRequest(), owner.getLogin()));
        ServiceException expected = new ServiceException(ErrorCode.IDEA_NOT_FOUND, "");
        assertAll(
                () -> {
                    assertEquals(ideaMapper.convertToDto(ideaAfterDelete), deletedResponse);
                    assertEquals(expected.getErrorCode(), actual.getErrorCode());
                }
        );


    }

    @Test
    public void testRemoveIdea__oneIdeaTwice__byOwner() throws NotFoundEntityException {
        User user = user();

        Idea ideaBeforeDelete = idea(1, user);
        Idea ideaAfterDelete = idea(ideaBeforeDelete.getId(), user, IdeaStatus.DELETED);

        Mockito.when(userRepo.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        Mockito.when(ideaRepo.findByIdAndUserIdAndStatusNot( ideaBeforeDelete.getId(), user.getId(), IdeaStatus.DELETED ))
                .thenReturn(Optional.of(ideaBeforeDelete))
                .thenReturn(Optional.empty());

        Mockito.when(ideaRepo.save(ideaAfterDelete)).thenReturn(ideaAfterDelete);

        IdeaDtoResponse actualAfterRemove = ideaService.removeIdea(ideaBeforeDelete.getId(), user.getLogin());
        IdeaDtoResponse expectedAfterRemove = ideaResponseDto(IdeaStatus.DELETED);

        ServiceException actualError = assertThrows(ServiceException.class, () -> ideaService.removeIdea(ideaBeforeDelete.getId(), user.getLogin()));
        ServiceException expectedError = new ServiceException(ErrorCode.IDEA_NOT_FOUND, String.format(ErrorCode.IDEA_NOT_FOUND.getTemplate(), ideaBeforeDelete.getId()));

        assertAll(
                () -> {
                    assertEquals(expectedAfterRemove, actualAfterRemove);
                    assertEquals(expectedError.getErrorCode(), actualError.getErrorCode());
                    assertEquals(expectedError.getMessage(), actualError.getMessage());
                }
        );
    }

    @Test
    public void testRemoveIdea__byNotOwner() {
        User owner = user();
        User other = user(2);

        Idea removedIdea = idea(1, owner);

        Mockito.when(userRepo.findByLogin(other.getLogin())).thenReturn(Optional.of(other));

        Mockito.when(ideaRepo.findByIdAndUserIdAndStatusNot(removedIdea.getId(), other.getId(), IdeaStatus.DELETED)).thenReturn(Optional.empty());

        ServiceException actualError = assertThrows(ServiceException.class, () -> ideaService.removeIdea(removedIdea.getId(), other.getLogin()));
        ServiceException expectedError = new ServiceException(ErrorCode.IDEA_NOT_FOUND, "");

        assertEquals(expectedError.getErrorCode(), actualError.getErrorCode());

    }

    @Test
    public void testRemoveIdea__userNotFoundByLogin() {
        User user = user();
        Idea idea = idea(1, user);

        Mockito.when(userRepo.findByLogin(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(ideaRepo.findById(idea.getId())).thenReturn(Optional.of(idea));

        ServiceException actual = assertThrows(ServiceException.class, () -> ideaService.removeIdea(1, user(2).getLogin()));
        ServiceException expected = new ServiceException(ErrorCode.USER_NOT_FOUND, "");

        assertEquals(expected.getErrorCode(), actual.getErrorCode());
    }

    @Test
    public void testRemoveIdea__afterUpdate() throws NotFoundEntityException {
        User user = user();
        Idea idea = idea(1, user);

        Mockito.when(userRepo.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        Mockito.when(ideaRepo.findByIdAndUserIdAndStatusNot(idea.getId(), user.getId(), IdeaStatus.DELETED)).thenReturn(Optional.of(idea));
        Mockito.when(ideaRepo.save(idea)).thenReturn(idea);

        IdeaDtoResponse updateResponse = ideaService.updateIdea(idea.getId(), saveIdeaDtoRequest("titleUpdate", "descUpdate"), user.getLogin());

        idea = idea(updateResponse.getId(), updateResponse.getTitle(), updateResponse.getDescription(), user, updateResponse.getCreationDate(), updateResponse.getLastUpdateDate(), updateResponse.getStatus());
        Idea ideaAfterDelete = idea(updateResponse.getId(), updateResponse.getTitle(), updateResponse.getDescription(), user, updateResponse.getCreationDate(), updateResponse.getLastUpdateDate(), IdeaStatus.DELETED);

        Mockito.when(ideaRepo.save(ideaAfterDelete)).thenReturn(ideaAfterDelete);

        IdeaDtoResponse removedResponse = ideaService.removeIdea(idea.getId(), user.getLogin());

        assertEquals(ideaMapper.convertToDto(ideaAfterDelete), removedResponse);
    }
}
