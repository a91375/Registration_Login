package tw.yen.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EmailTest {
	@Autowired
	private JavaMailSender mailsender;
	
	@Test
	public void sendTestMail() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("annie91375@icloud.com");
		message.setSubject("Test EMail");
		message.setText("This is a test mail");
		mailsender.send(message);
	}
	
}
