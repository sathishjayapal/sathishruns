package me.sathish;

import me.sathish.common.ContainersConfig;
import org.springframework.boot.SpringApplication;

public class TestApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "test");
        SpringApplication.from(Application::main).with(ContainersConfig.class).run(args);
    }
}
