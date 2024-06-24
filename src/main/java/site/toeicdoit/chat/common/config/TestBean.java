package site.toeicdoit.chat.common.config;

import org.springframework.stereotype.Component;

@Component
public class TestBean {
    public void dependencyCheck() {
        System.out.println("Dependency check");
    }
}
