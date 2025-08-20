package tw.yen.spring.security.enums;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
	Admin(Set.of("READ_PROFILE", "MANAGE_USERS", "VIEW_REPORTS")),
	User(Set.of("READ_PROFILE", "MANAGE_USERS"));
	
	@Getter
    private final Set<String> permissions;
	
	public Set<SimpleGrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		// 附加功能權限
        this.permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
        return authorities;
		
	}
	
}
