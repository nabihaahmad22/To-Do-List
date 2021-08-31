package com.example.todolist;

public class Model {

    String task,description,email,date;

    public Model() {
    }

    public Model(String task, String description, String email, String date) {
        this.task = task;
        this.description = description;
        this.email = email;
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
