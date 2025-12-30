package net.xmercerweiss.budgeteer2.data;


/**
 * Represents the dated and titled transfer of an arbitrary amount of an arbitrary currency
 * @param epochDay The day the transaction occurred as a number of days before or since 1970-01-01
 * @param amount The number of units of currency (Cents, Yen, Pence, etc.) involved
 * @param title The title of the transaction, not null
 * @author Xavier Mercerweiss
 * @version v2.0 2025-12-30
 */
public record Transaction(long epochDay, long amount, String title)
{
}
