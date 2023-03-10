package com.example.OrderService.Service;

import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.InfoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCoreServiceTest {

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserCoreService userCoreService;

    private final UUID uuid1=UUID.randomUUID();






    private User mockUser(String username){
        return User.builder()
                .id(uuid1).username(username).fullName("admin").password("admin")
                .score(3.0)
                .build();
    }

    @Test
    void getProfile()  {

        when(userRepository.findById(uuid1)).thenReturn(Optional.ofNullable(mockUser("admin")));

        InfoResponse response=userCoreService.getProfile(uuid1);

        Assertions.assertEquals("admin",response.getUsername());
    }



    @Test //check case if already have the username
    void updateUserRepeatedUsername() {
        UserCoreService Spy=spy(userCoreService);
        when(userRepository.findById(uuid1)).thenReturn(Optional.ofNullable(mockUser("admin")));
        doReturn(mockUser("alex")).when(Spy).saveAndReturn(mockUser(any()));

        String res=Spy.updateUser(mockUser("alex"));

        Assertions.assertEquals("Successful update",res);

    }


    @Test
    void verifyCanOrder() {

        when(userRepository.findById(uuid1)).thenReturn(Optional.ofNullable(mockUser("admin")));

        Boolean res=userCoreService.VerifyCanOrder(uuid1);

        Assertions.assertEquals(true,res);

    }
    @Test
    void CannotOrder() {
        User user=mockUser("admin");
        user.setScore(1.9);
        when(userRepository.findById(uuid1)).thenReturn(Optional.ofNullable(user));

        Boolean res=userCoreService.VerifyCanOrder(uuid1);

        Assertions.assertEquals(false,res);

    }
}