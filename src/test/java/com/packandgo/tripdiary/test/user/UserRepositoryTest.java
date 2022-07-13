package com.packandgo.tripdiary.test.user;


import com.packandgo.tripdiary.enums.UserStatus;
import com.packandgo.tripdiary.model.Role;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.repository.RoleRepository;
import com.packandgo.tripdiary.repository.TripRepository;
import com.packandgo.tripdiary.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.hibernate.annotations.DynamicUpdate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;

import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    @Order(0)
    @DisplayName("Test all repositories are not null")
    public void testRepository() {
        Assertions.assertNotNull(tripRepository);
        Assertions.assertNotNull(userRepository);
        Assertions.assertNotNull(roleRepository);
    }

    @Test
    @DisplayName("Test insert user successfully")
    @Rollback(false)
    @Order(1)
    public void userTest1_saveUserTest1() {

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
        Assert.isTrue(user.getVerifyToken() != null);

        Set<Role> saveUserRoles = savedUser.getRoles();
        Assert.isTrue(saveUserRoles
                .stream().filter(r -> r.getName() == role.getName())
                .collect(Collectors.toSet()).size() == 1);
    }

    @Test
    @DisplayName("Test when insert user without email")
    @Order(2)
    public void userTest2_saveUserTest2() {
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setRoles(roles);

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
        });

    }

    @Test
    @DisplayName("Test when insert user without username")
    @Order(3)
    public void userTest3_saveUserTest3() {
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setRoles(roles);

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    @DisplayName("Test when insert user without password")
    @Order(4)
    public void userTest4_saveUserTest4() {
        Role role = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setUsername("user");
        user.setRoles(roles);

        Throwable exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
        });

    }


    @Test
    @DisplayName("Test get user by user's ID")
    @Order(5)
    public void userTest5_getUserTest1() {
        int id = 1;
        User user = userRepository.findById(Long.valueOf(id)).orElse(null);
        Set<Role> roles = user.getRoles();

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getId() == id);
        Assertions.assertTrue("test@gmail.com".equals(user.getEmail()));
        Assertions.assertTrue("user".equals(user.getUsername()));
        Assertions.assertTrue("password".equals(user.getPassword()));
        Assert.isTrue(user.getStatus().equals(UserStatus.INACTIVE));

        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.size() > 0);
        Assertions.assertTrue(roles.stream()
                .filter(r -> "USER".equals(r.getName()))
                .collect(Collectors.toList())
                .size() > 0);
    }

    @Test
    @DisplayName("Test get user with ID not exist")
    @Order(6)
    public void userTest6_getUserTest2() {
        int id = 10;
        User user = userRepository.findById(Long.valueOf(id)).orElse(null);

        Assertions.assertNull(user);
    }

    @Test
    @DisplayName("Test get user with username")
    @Order(7)
    public void userTest7_getUserTest3() {
        String username = "user";
        User user = userRepository.findByUsername(username).orElse(null);
        Set<Role> roles = user.getRoles();

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getId() == 1);
        Assertions.assertTrue("test@gmail.com".equals(user.getEmail()));
        Assertions.assertTrue("user".equals(user.getUsername()));
        Assertions.assertTrue("password".equals(user.getPassword()));
        Assert.isTrue(user.getStatus().equals(UserStatus.INACTIVE));
        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.size() > 0);
        Assertions.assertTrue(roles.stream()
                .filter(r -> "USER".equals(r.getName()))
                .collect(Collectors.toList())
                .size() > 0);
    }

    @Test
    @DisplayName("Test get user with username not existed")
    @Order(8)
    public void userTest8_getUserTest4() {
        String username = "userabc";
        User user = userRepository.findByUsername(username).orElse(null);
        Assertions.assertNull(user);
    }

    @Test
    @DisplayName("Test get user with email")
    @Order(9)
    public void userTest9_getUserTest5() {
        String email = "test@gmail.com";
        User user = userRepository.findByEmail(email).orElse(null);
        Set<Role> roles = user.getRoles();

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getId() == 1);
        Assertions.assertTrue("test@gmail.com".equals(user.getEmail()));
        Assertions.assertTrue("user".equals(user.getUsername()));
        Assertions.assertTrue("password".equals(user.getPassword()));
        Assert.isTrue(user.getStatus().equals(UserStatus.INACTIVE));
        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.size() > 0);
        Assertions.assertTrue(roles.stream()
                .filter(r -> "USER".equals(r.getName()))
                .collect(Collectors.toList())
                .size() > 0);
    }

    @Test
    @DisplayName("Test get user with email or username")
    @Order(10)
    public void userTest10_getUserTest6() {
        String keyword = "user";
        User user = userRepository.findByUsernameOrEmail(keyword, keyword).orElse(null);
        Set<Role> roles = user.getRoles();

        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getId() == 1);
        Assertions.assertTrue("test@gmail.com".equals(user.getEmail()));
        Assertions.assertTrue("user".equals(user.getUsername()));
        Assertions.assertTrue("password".equals(user.getPassword()));
        Assert.isTrue(user.getStatus().equals(UserStatus.INACTIVE));

        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.size() > 0);
        Assertions.assertTrue(roles.stream()
                .filter(r -> "USER".equals(r.getName()))
                .collect(Collectors.toList())
                .size() > 0);
    }


    @Test
    @DisplayName("Test update user successfully")
    @Order(11)
    public void userTest11_updateUserTest1() {
        long id = 1;
        User user = userRepository.findById(id).orElse(null);

        user.setUsername("new_username");
        user.setEmail("new_email");
        user.setPassword("new_password");

        userRepository.save(user);

        User updatedUser = userRepository.findById(id).orElse(null);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(id, updatedUser.getId());
        Assertions.assertEquals("new_username", user.getUsername());
        Assertions.assertEquals("new_email", user.getEmail());
        Assertions.assertEquals("new_password", user.getPassword());
        Assertions.assertEquals(user.getStatus(), updatedUser.getStatus());
        Assertions.assertEquals(user.getRoles().size(), updatedUser.getRoles().size());
        Assertions.assertEquals(
                user.getRoles().stream().findAny().get().getName(),
                updatedUser.getRoles().stream().findAny().get().getName()
        );
    }

    @Test
    @DisplayName("Test update user without password")
    @Order(12)
    public void userTest12_updateUserTest3() {
        long id = 1;
        User user = userRepository.findById(id).orElse(null);

        user.setUsername("new_username");
        user.setEmail("new_email");
        user.setPassword(null);

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    @DisplayName("Test delete user(existed user)")
    @Order(13)
    public void userTest13_deleteUserTest1() {
        long id = 1;
        userRepository.deleteById(id);

        User userAfterDeteted = userRepository.findById(id).orElse(null);
        Assertions.assertNull(userAfterDeteted);
        Assertions.assertEquals(0, userRepository.findAll().size());
    }


    @Test
    @DisplayName("Test delete user(unexisted user)")
    @Order(14)
    public void userTest14_deleteUserTest2() {
        long id = 10;

        Throwable exception = Assertions.assertThrows(Exception.class, () -> {
            userRepository.deleteById(id);
        });

        Assertions
                .assertTrue(
                        exception
                                .getMessage()
                                .contains("No class com.packandgo.tripdiary.model.User entity with id " +  id +  " exists!"));

        Assertions.assertEquals(1, userRepository.findAll().size());

    }


    @Test
    @DisplayName("Test get all users")
    @Order(15)
    public void userTest15_getAllUsersTest() {

        Role role = new Role("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        User newUser = new User();
        newUser.setUsername("new_user");
        newUser.setEmail("new_test@gmail.com");
        newUser.setPassword("new_password");
        newUser.setRoles(roles);


        userRepository.save(newUser);

        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());

        User user0 = users.get(0);
        Assertions.assertEquals(1, user0.getId());
        Assertions.assertEquals("user", user0.getUsername());
        Assertions.assertEquals("test@gmail.com", user0.getEmail());
        Assertions.assertEquals("password", user0.getPassword());

        Assertions.assertEquals(1, user0.getRoles().size());
        Assertions.assertEquals("USER", user0.getRoles().stream().findFirst().get().getName());

        User user1 = users.get(1);
        Assertions.assertEquals(2, user1.getId());
        Assertions.assertEquals("new_user", user1.getUsername());
        Assertions.assertEquals("new_test@gmail.com", user1.getEmail());
        Assertions.assertEquals("new_password", user1.getPassword());

        Assertions.assertEquals(1, user1.getRoles().size());
        Assertions.assertEquals("ADMIN", user1.getRoles().stream().findFirst().get().getName());

    }
}
