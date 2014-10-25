package org.community.ridiculous.todomvc;

public class Todo {
	
	String title;
	boolean completed = false;

	public Todo(String title, boolean completed) {
		this.title = title;
		this.completed = completed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
