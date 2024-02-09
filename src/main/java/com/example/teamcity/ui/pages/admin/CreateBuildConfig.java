package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class CreateBuildConfig extends Page {
    private SelenideElement urlInput = element(Selectors.byId("url"));
    private SelenideElement usernameInput = element(Selectors.byId("username"));
    private SelenideElement passwordInput = element(Selectors.byId("password"));
    private SelenideElement error = element(Selectors.byId("errorExternalId"));
    public CreateBuildConfig open(String parentProjectId){
        Selenide.open("/admin/createObjectMenu.html?projectId="+parentProjectId+"&showMode=createBuildTypeMenu");
        waitUntilPageIsLoaded();
        return this;
    }
    public CreateBuildConfig setupBuildConfig(String url, String username, String password){
        urlInput.clear();
        urlInput.sendKeys(url);
        usernameInput.clear();
        usernameInput.sendKeys(username);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        submit();
        waitUntilDataIsSaved();
        return this;
    }
    public void submitSecondStep(){
        submit();
        waitUntilDataIsSaved();
    }
    public void checkError() {
        error.shouldBe();
    }

}
