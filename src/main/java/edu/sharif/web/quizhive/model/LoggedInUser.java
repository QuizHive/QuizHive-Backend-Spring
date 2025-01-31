package edu.sharif.web.quizhive.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoggedInUser extends org.springframework.security.core.userdetails.User {
	private final User user;

	public LoggedInUser(User user, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.user = user;
	}

	public LoggedInUser(User user, String username, String password, boolean enabled, boolean accountNonExpired,
	                    boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.user = user;
	}

	public User get() {
		return user;
	}
}
