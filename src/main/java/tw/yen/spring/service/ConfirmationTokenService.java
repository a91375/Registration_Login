package tw.yen.spring.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import tw.yen.spring.entity.ConfirmationTokens;
import tw.yen.spring.repository.ConfirmationTokenRepository;

@Service
public class ConfirmationTokenService {
	private final ConfirmationTokenRepository confirmationTokenRepository;
	
	public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
		this.confirmationTokenRepository = confirmationTokenRepository;
	}
	
	public void saveToken(ConfirmationTokens token) {
		confirmationTokenRepository.save(token);
	}
	
	public Optional<ConfirmationTokens> getToken(String token) {
		return confirmationTokenRepository.findByToken(token);
	}
	
	public String getUEmail(String token) {
		return confirmationTokenRepository.findEmailByToken(token);
	}
	
	public int setConfirmedAt(String token) {
		return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
	}
	
}
