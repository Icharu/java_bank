package org.example.model;

import static org.example.model.BankService.INVESTMENT;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class InvestmentWallet extends Wallet {
    private final Investment investment;

    private final AccountWallet account;

    public InvestmentWallet(final Investment investment, final AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), getService(), "Valor investido na aplicação");
    }

    public void updateAmount(final long percentage) {
        var amount = getFunds() * percentage / 100;
        var history = new MoneyAudit(
            java.util.UUID.randomUUID(),
            getService(),
            "Rendimento da aplicação",
            java.time.OffsetDateTime.now()
        );
        var money = java.util.stream.Stream.generate(() -> new Money(history)).limit(amount).toList();
        this.money.addAll(money);
    }
}
