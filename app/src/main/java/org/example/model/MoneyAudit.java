package org.example.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MoneyAudit(
    UUID id,
    BankService targetService,
    String description,
    OffsetDateTime createdAt
) {}
