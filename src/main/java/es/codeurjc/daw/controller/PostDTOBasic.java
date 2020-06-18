package es.codeurjc.daw.controller;

public class PostDTOBasic {

	private long id;
	private String title;

	public PostDTOBasic() {
	}

	public PostDTOBasic(String title) {
		this.title = title;
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

	@Override
	public String toString() {
		return "Post: {id: " + this.id + ", title: '" + this.title + "'}";
	}

}
