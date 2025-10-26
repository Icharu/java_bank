package org.example.repository;


import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import lombok.NoArgsConstructor;
@NoArgsConstructor(access = PRIVATE)
public final class CommonsRepository {
    public static void checkFundsForTransaction(final AccountWallet source, final long amount) {
        if (source.getTotalFunds() < amount) {
            throw new org.example.exception.NoFundsEnoughException(
                "Não tem dinheiro suficiente"
            );
        }
    }
    public static List<Money> generateMoney(final UUID id, final long funds, final String description) {
        var history = new MoneyAudit(
            id,
            null,
            description,
            java.time.OffsetDateTime.now()
        );
        return java.util.stream.Stream.generate(() -> new Money(history)).limit(funds).toList();
    }
}
