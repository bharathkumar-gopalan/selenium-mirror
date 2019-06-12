package com.bharath.experiments.mirror.application;

import com.bharath.experiments.mirror.internal.CommandHandler;
import com.bharath.experiments.mirror.internal.SessionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class Application {

    @Bean
    public SessionManager getSessionManager(){
        return new SessionManager();
    }

    @Bean
    public CommandHandler getCommandHandler(){
        return new CommandHandler();
    }

    public static void main(String args[]){
        SpringApplication.run(Application.class, args);
    }
}
