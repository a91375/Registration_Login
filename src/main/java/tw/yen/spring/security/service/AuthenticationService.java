package tw.yen.spring.security.service;

import tw.yen.spring.payload.request.AuthenticationRequest;
import tw.yen.spring.payload.request.RegistrationRequest;
import tw.yen.spring.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
