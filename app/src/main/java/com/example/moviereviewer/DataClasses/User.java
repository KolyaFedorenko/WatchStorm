package com.example.moviereviewer.DataClasses;

public class User {

    private String login;
    private String password;
    private String pathToImage;

    public User() { }

    public User(String login, String password, String pathToImage) {
        this.login = login;
        this.password = password;
        this.pathToImage = pathToImage;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }
}
