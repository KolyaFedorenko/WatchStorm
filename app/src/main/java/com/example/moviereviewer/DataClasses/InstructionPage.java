package com.example.moviereviewer.DataClasses;

public class InstructionPage {
    private String pageTitle;
    private String pageDescription;

    public InstructionPage(String pageTitle, String pageDescription) {
        this.pageTitle = pageTitle;
        this.pageDescription = pageDescription;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }
}
