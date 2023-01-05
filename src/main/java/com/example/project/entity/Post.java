package com.example.project.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.repository.Query;

import com.example.project.dto.PostRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Post extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<Comment> comments = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "liked_user_the_post",
		joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "liked_user_id", referencedColumnName = "id")
	)
	private List<User> likes = new ArrayList<>();

	public Post(User user, String title, String content) {
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public void update(PostRequestDto postRequestDto){
		this.title = postRequestDto.getTitle();
		this.content = postRequestDto.getContent();
	}

	public boolean checkAuthorization(User user) {
		return Objects.equals(this.user.getId(), user.getId());
	}
}