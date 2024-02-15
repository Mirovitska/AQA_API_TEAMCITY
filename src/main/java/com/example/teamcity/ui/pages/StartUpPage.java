package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.element;

@Getter
public class StartUpPage extends Page {
    private final SelenideElement acceptLicense = $("#accept");

    private final SelenideElement proceedButton = $("#proceedButton");
    private final SelenideElement restoreFromBackupButton = $("#restoreButton");
    private final SelenideElement dbTypeSelect = $("#dbType");

    private final SelenideElement header = element("input[id='header']");
    private final SelenideElement backFileUploaded = element("input[name='backupFile']");
    private final SelenideElement agreementPage = element("#agreementPage");

    public static StartUpPage open() {
        return Selenide.open("/", StartUpPage.class);
    }


    public void waitUntilAgreementPageIsOpen() {
        agreementPage.shouldBe(Condition.visible, Duration.ofMinutes(3));
    }
    public void waitUntilDataBasePageIsOpen() {
        dbTypeSelect.shouldBe(Condition.visible, Duration.ofMinutes(3));
    }
    public StartUpPage setupTeamCityServer() {
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilDataBasePageIsOpen();
        proceedButton.click();
        waitUntilAgreementPageIsOpen();
        acceptLicense.should(exist).scrollTo().click();
        submit();
        return this;
    }
}
