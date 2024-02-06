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
    private SelenideElement acceptLicence = $("#accept");
    ;
    private SelenideElement proceedButton = $("#proceedButton");
    private SelenideElement submitButton = element("input[id='submit']");
    private SelenideElement restoreFromBackupButton = $("#restoreButton");

    private SelenideElement header = element("input[id='header']");
    private SelenideElement backFileUploaded = element("input[name='backupFile']");

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
