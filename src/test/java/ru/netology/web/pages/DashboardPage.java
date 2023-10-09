package ru.netology.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id='dashboard']");
    private ElementsCollection cards = $$(".list__item div");
    private String balanceStart = "баланс: ";
    private String balanceFinish = " р.";

    public DashboardPage() {

        heading.shouldBe(visible);
    }

    // получить значение баланса по индексу
    public int getBalanceValue(int cardIndex) {
        var text = cards.get(cardIndex).getText();
        return extractBalance(text);
    }

    // кликнуть на кнопку "Пополнить"
    public TransferPage selectCard(DataHelper.Card card) {
        cards.findBy(attribute("data-test-id", card.getDataTestId())).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}