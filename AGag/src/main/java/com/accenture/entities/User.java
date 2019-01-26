package com.accenture.entities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;

import javax.persistence.*;

import com.accenture.utils.Util;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails, Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Lob
	@Column(nullable=false, columnDefinition="mediumblob")
	private byte[] profilePic;
	
	private String nickname;
	
	private String email;
	
	private int numberOfPosts;
	
	private int numberOfComments;
	
    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

	@ManyToMany( fetch = FetchType.EAGER,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE,
			})
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> authorities;
	
    @OneToMany
	private List<Gag> posts;

    @OneToMany
	private List<Comment> comments;
	
	public User() {
		numberOfComments = 0;
		numberOfPosts = 0;
		profilePic = getDefaultProfilePic();
		nickname = getUsername();
		authorities = new HashSet<>();
		posts = new ArrayList<>();
		comments = new ArrayList<>();
		isAccountNonExpired = true;
		isAccountNonLocked = true;
		isCredentialsNonExpired = true;
		isEnabled = true;
		
	}



	public User(String username,
				String password,
				byte[] profilePic,
				String nickname,
				String email,
				int numberOfPosts,
				int numberOfComments,
				boolean isAccountNonExpired,
				boolean isAccountNonLocked,
				boolean isCredentialsNonExpired,
				boolean isEnabled,
				Set<Role> authorities,
				List<Gag> posts,
				List<Comment> comments) {
		this.username = username;
		this.password = password;
		this.profilePic = profilePic;
		this.nickname = nickname;
		this.email = email;
		this.numberOfPosts = numberOfPosts;
		this.numberOfComments = numberOfComments;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
		this.isEnabled = isEnabled;
		this.authorities = authorities;
		this.posts = posts;
		this.comments = comments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getNumberOfPosts() {
		return numberOfPosts;
	}

	public void setNumberOfPosts(int numberOfPosts) {
		this.numberOfPosts = numberOfPosts;
	}

	public int getNumberOfComments() {
		return numberOfComments;
	}

	public void setNumberOfComments(int numberOfComments) {
		this.numberOfComments = numberOfComments;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		isAccountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		isAccountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		isCredentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public void setAuthorities(Set<Role> authorities) {
		this.authorities = authorities;
	}

	public List<Gag> getPosts() {
		return posts;
	}

	public void setPosts(List<Gag> posts) {
		this.posts = posts;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public Set<Role> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}


	private byte[] getDefaultProfilePic(){
		byte[] result = null;
		try {
			result =  Files.readAllBytes(new File(Util.DEFAULT_PIC_PATH).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
