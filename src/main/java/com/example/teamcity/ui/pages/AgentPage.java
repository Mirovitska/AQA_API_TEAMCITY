package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class AgentPage extends Page{

    private final SelenideElement authorizeAgentButton = $(".AuthorizeAgent__authorizeAgent--Xr > button");
    private final SelenideElement authorizeAgentModalWindowButton = $(".CommonForm__button--Nb");

    public AgentPage() {
        authorizeAgentButton.shouldBe(Condition.visible);
    }

    public static AgentPage open() {
        return Selenide.open("/agents/unauthorized", AgentPage.class);
    }

    public AgentPage authorizeAgent() {
        authorizeAgentButton.click();
        authorizeAgentModalWindowButton.shouldBe(Condition.visible).click();
        return this;
    }

}
