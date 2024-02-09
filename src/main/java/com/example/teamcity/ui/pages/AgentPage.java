package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Getter
public class AgentPage extends Page {

    private final SelenideElement authorizeButton = $x("//div[contains(@class, 'AuthorizeAgent__authorizeAgent--Xr')]//button");
    private final SelenideElement authorizeButtonOnModal = $x("//button[contains(@class, 'CommonForm__button--Nb')]");


    public void authorizeAgent() {
        authorizeButton.click();
        authorizeButtonOnModal.shouldBe(visible, Duration.ofMinutes(3)).click();
    }

    public static AgentPage open() {
        return Selenide.open("/agents/unauthorized", AgentPage.class);
    }


}
