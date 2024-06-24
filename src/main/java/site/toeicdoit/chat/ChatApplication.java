package site.toeicdoit.chat;

import site.toeicdoit.chat.common.config.TestBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

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
