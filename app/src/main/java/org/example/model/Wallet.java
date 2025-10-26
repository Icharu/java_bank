package org.example.model;

import java.lang.reflect.Constructor;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@ToString
public abstract class Wallet {
    @Getter
    private final BankService service;

    protected final List<Money> money;

    public Wallet(final BankService serviceType) {
        this.service = serviceType;
        this.money = new ArrayList<>();
    }

    protected  List<Money> generateMoney(final long amount, final String description) {
        var history = new MoneyAudit(UUID.randomUUID(), service, description, java.time.OffsetDateTime.now());
        return Stream.generate(() -> new Money(history)).limit(amount).toList();
    }
    public Long getFunds() {
        return money.size();
    }
    public void addMoney(final List<Money> money, final BankService service, final String description) {
        var history = new MoneyAudit(UUID.randomUUID(), service, description, java.time.OffsetDateTime.now());
        money.forEach(m -> m.addHistory(history));
        this.money.addAll(money);
    }
    public List<Money> reduceMoney(final long amount) {
        List<Money> reduced = new ArrayList<>();
        for (int i = 0; i < amount && !money.isEmpty(); i++) {
            reduced.add(this.money.removeFirst());
        }
        return reduced;
    }
    public List<MoneyAudit> getFinancialTransactions() {
        return money.stream()
                .flatMap(m -> m.getHistory().stream())
                .toList()();
    }
}