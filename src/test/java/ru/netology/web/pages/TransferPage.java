package ru.netology.web.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement headingOfTransferPage = $(byText("Пополнение карты"));
    private SelenideElement amount = $("[data-test-id='amount'] input");
    private SelenideElement fromWhichCard = $("[data-test-id='from'] input");
    // private SelenideElement toWhichCard = $("[data-test-id='to']");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public TransferPage() {
        headingOfTransferPage.shouldBe(visible);
    }

    public DashboardPage doValidTransfer(String amountToTransfer, DataHelper.Card card) {
        doTransfer(amountToTransfer, card);
        return new DashboardPage();
    }

    public void doTransfer(String amountToTransfer, DataHelper.Card card) {
        amount.setValue(amountToTransfer);
        fromWhichCard.setValue(card.getNumber());
        transferButton.click();
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(visible);
        errorNotification.shouldHave(text("Ошибка"));
    }
}
