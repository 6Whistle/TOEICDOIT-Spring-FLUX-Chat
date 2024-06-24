package site.toeicdoit.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import site.toeicdoit.chat.config.TestBean;

@RequiredArgsConstructor
@SpringBootApplication
public class ChatApplication {

	private final TestBean testBean;

	@PostConstruct
	public void dependencyCheck() {
		testBean.dependencyCheck();
	}

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

}
