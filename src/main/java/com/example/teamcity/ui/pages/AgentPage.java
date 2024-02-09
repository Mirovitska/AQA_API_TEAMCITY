package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Getter
public class AgentPage extends Page {

    private final SelenideElement authorizeButton = $x("//div[contains(@class, 'AuthorizeAgent__authorizeAgent--Xr')]//button");
    private final SelenideElement authorizeButtonOnModal = $x("//button[contains(@class, 'CommonForm__button--Nb')]");


    public AgentPage authorizeAgent() {
        authorizeButton.click();
        waitUntilPageIsLoaded();
        authorizeButtonOnModal.shouldBe(visible).click();
        return this;
    }

    public static AgentPage open() {
        return Selenide.open("/agents/unauthorized", AgentPage.class);
    }
}
