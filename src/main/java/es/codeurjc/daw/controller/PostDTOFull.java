package es.codeurjc.daw.controller;

import java.util.ArrayList;
import java.util.List;

public class PostDTOFull {

	private long id;
	private String title;
	private String content;

	private List<CommentDTOFull> comments = new ArrayList<>();

	public PostDTOFull() {}

	public PostDTOFull(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<CommentDTOFull> getComments() {
		return comments;
	}

	public void setComments(List<CommentDTOFull> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Post: {id: " + this.id + ", title: '" + this.title + "' , content: '" + this.content + "', comments: "+ comments.size() +"}";
	}

}
