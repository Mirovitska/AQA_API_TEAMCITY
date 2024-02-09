package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
@Getter
public class AgentPage extends Page {

    private final SelenideElement authorizeAgentButton = $(".AuthorizeAgent__authorizeAgent--Xr > button");
    private final SelenideElement authorizeAgentModalWindowButton = $(".CommonForm__button--Nb");


    public AgentPage authorizeAgent() {
        authorizeAgentButton.click();
        waitUntilPageIsLoaded();
        authorizeAgentModalWindowButton.shouldBe(visible).click();
        return this;
    }

//    private final SelenideElement commentField = $(By.xpath("//*[@data-test='ring-input']//*[contains(@id, 'ring-input')]"));
//    private final SelenideElement authorizeButton = $(By.xpath("//button//*[contains(text(), 'Authorize')]"));
//    private final SelenideElement authorizeModalButton = $(By.xpath("//*[@data-test=\"ring-island-content\"]//button//*[contains(text(), 'Authorize')]"));
//    private final SelenideElement agentLink = $(By.xpath("//*[contains(@title, 'Agent name:')]"));
//
//    private SelenideElement authStatus = $(By.xpath("//*[@data-agent-authorization-status=\"true\"]//span[1]"));

    public static AgentPage open() {
        return Selenide.open("/agents/unauthorized", AgentPage.class);
    }
}
//
//    public AgentPage authTeamCityAgent() {
//        waitUntilPageIsLoaded();
//        authorizeButton.click();
//        waitUntilPageIsLoaded();
//        commentField.shouldBe(Condition.enabled, Duration.ofSeconds(10));
//        commentField.sendKeys("test");
//        waitUntilPageIsLoaded();
//        authorizeModalButton.click();
//        waitUntilPageIsLoaded();
//        agentLink.click();
//        return this;
//    }
//}
