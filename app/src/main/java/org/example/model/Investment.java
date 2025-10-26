package org.example.model;

public record Investment(
    long id,
    long tax,
    long daysToRescue,
    long initialFunds) {}