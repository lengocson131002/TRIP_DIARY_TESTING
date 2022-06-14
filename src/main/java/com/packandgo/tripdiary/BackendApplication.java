package com.packandgo.tripdiary;

import com.packandgo.tripdiary.model.PasswordResetToken;
import com.packandgo.tripdiary.repository.PasswordResetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.transaction.Transactional;

@SpringBootApplication
@EnableWebMvc
@EnableScheduling
public class BackendApplication{

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
