package ru.netology.web.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.pages.DashboardPage;
import ru.netology.web.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class MoneyTransferTest {

    DashboardPage dashboardPage;

    @BeforeEach
    void beforeEach() {
        //open("http://localhost:9999");
        //var loginPage = new LoginPage();
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromSecondToFirstCard() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getBalanceValue(0);
        var secondCardBalance = dashboardPage.getBalanceValue(1);
        var amountToTransfer = DataHelper.getValidTransferAmount(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance + amountToTransfer;
        var expectedBalanceSecondCard = secondCardBalance - amountToTransfer;

        var transferPage = dashboardPage.selectCard(firstCard);
        dashboardPage = transferPage.doValidTransfer(String.valueOf(amountToTransfer), secondCard);

        var actualBalanceFirstCard = dashboardPage.getBalanceValue(0);
        var actualBalanceSecondCard = dashboardPage.getBalanceValue(1);

        Assertions.assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        Assertions.assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldTransferFromFirstToSecondCard() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getBalanceValue(0);
        var secondCardBalance = dashboardPage.getBalanceValue(1);
        var amountToTransfer = DataHelper.getValidTransferAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amountToTransfer;
        var expectedBalanceSecondCard = secondCardBalance + amountToTransfer;

        var transferPage = dashboardPage.selectCard(secondCard);
        dashboardPage = transferPage.doValidTransfer(String.valueOf(amountToTransfer), firstCard);

        var actualBalanceFirstCard = dashboardPage.getBalanceValue(0);
        var actualBalanceSecondCard = dashboardPage.getBalanceValue(1);

        Assertions.assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        Assertions.assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldReturnErrorNotificationWhenInvalidTransfer() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();
        var firstCardBalance = dashboardPage.getBalanceValue(0);
        var amountToTransfer = DataHelper.getInvalidTransferAmount(firstCardBalance);

        var transferPage = dashboardPage.selectCard(secondCard);
        transferPage.doTransfer(String.valueOf(amountToTransfer), firstCard);

        transferPage.checkErrorNotification();
    }
}