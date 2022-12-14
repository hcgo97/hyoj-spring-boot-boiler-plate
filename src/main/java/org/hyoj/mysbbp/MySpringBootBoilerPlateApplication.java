package org.hyoj.mysbbp;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT5M")
@ComponentScan("org.hyoj")
@EntityScan("org.hyoj")
@EnableJpaRepositories("org.hyoj")
@ServletComponentScan("org.hyoj")
@SpringBootApplication
public class MySpringBootBoilerPlateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootBoilerPlateApplication.class, args);
    }

}
