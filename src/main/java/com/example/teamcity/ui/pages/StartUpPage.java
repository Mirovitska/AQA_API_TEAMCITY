package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.element;

@Getter
public class StartUpPage extends Page {
    private final SelenideElement acceptLicence = $("#accept");

    private final SelenideElement proceedButton = $("#proceedButton");
    private final SelenideElement submitButton = element("input[id='submit']");
    private final SelenideElement restoreFromBackupButton = $("#restoreButton");

    private final SelenideElement header = element("input[id='header']");
    private final SelenideElement backFileUploaded = element("input[name='backupFile']");

    public static StartUpPage open() {
        return Selenide.open("/", StartUpPage.class);
    }

    public StartUpPage setupTeamCityServer() {
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        acceptLicence.shouldBe(Condition.enabled, Duration.ofSeconds(5));
        acceptLicence.scrollTo();
        acceptLicence.click();
        submitButton.click();
        return this;
    }
}
