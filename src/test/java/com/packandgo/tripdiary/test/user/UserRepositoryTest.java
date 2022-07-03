package com.packandgo.tripdiary.test.user;


import com.packandgo.tripdiary.enums.UserStatus;
import com.packandgo.tripdiary.model.Role;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.repository.RoleRepository;
import com.packandgo.tripdiary.repository.TripRepository;
import com.packandgo.tripdiary.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Test insert user successfully")
    @Rollback(false)
    public void saveUserTest1() {

        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        User user = new User();
        user.setUsername("user");
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setRoles(roles);


        userRepository.save(user);

        User savedUser = userRepository.findById(Long.valueOf(1)).orElse(null);

        Assert.notNull(savedUser);
        Assert.isTrue(savedUser.getId() == 1);
        Assert.isTrue(user.getUsername().equals(savedUser.getUsername()));
        Assert.isTrue(user.getEmail().equals(savedUser.getEmail()));
        Assert.isTrue(user.getPassword().equals(savedUser.getPassword()));
        Assert.isTrue(user.getStatus().equals(UserStatus.INACTIVE));

        Set<Role> saveUserRoles = savedUser.getRoles();
        Assert.isTrue(saveUserRoles
                .stream().filter(r -> r.getName() == role.getName())
                .collect(Collectors.toSet()).size() == 1);
    }

    @Test
    @DisplayName("Test when insert user without email")
    public void saveUserTest2() {
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setRoles(roles);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });

    }

    @Test
    @DisplayName("Test when insert user without username")
    public void saveUserTest3() {
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setRoles(roles);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    @DisplayName("Test get user by user's ID")
    public void getUserTest1() {
        int id = 1;
        User user = userRepository.findById(Long.valueOf(id)).orElse(null);
        Set<Role> roles = user.getRoles();

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getId() == id);
        Assertions.assertTrue("test@gmail.com".equals(user.getEmail()));
        Assertions.assertTrue("user".equals(user.getUsername()));
        Assertions.assertTrue("password".equals(user.getPassword()));
        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.size() > 0);
        Assertions.assertTrue(roles.stream()
                .filter(r -> "USER".equals(r.getName()))
                .collect(Collectors.toList())
                .size() > 0);
    }

    @Test
    @DisplayName("Test get user with ID not exist")
    public void getUserTest2() {
        int id = 10;
        User user = userRepository.findById(Long.valueOf(id)).orElse(null);

        Assertions.assertNull(user);

    }

    @Test
    @DisplayName("Test get user with username")
    public void getUserTest3() {
        String username = "user";
        User user = userRepository.findByUsername("user").orElse(null);
        Set<Role> roles = user.getRoles();

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getId() == 1);
        Assertions.assertTrue("test@gmail.com".equals(user.getEmail()));
        Assertions.assertTrue("user".equals(user.getUsername()));
        Assertions.assertTrue("password".equals(user.getPassword()));
        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.size() > 0);
        Assertions.assertTrue(roles.stream()
                .filter(r -> "USER".equals(r.getName()))
                .collect(Collectors.toList())
                .size() > 0);

    }


//    @Test
//    public void updateUserTest1() {
//
//    }
//
//    @Test
//    public void updateUserTest2() {
//
//    }
//
//    @Test
//    public void getUserByIdTest1() {
//
//    }
//
//    @Test
//    public void getUserByIdTest2(){
//
//    }

}
