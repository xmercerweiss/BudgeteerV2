package net.xmercerweiss.budgeteer2.data;


/**
 * Represents the profile and preferences of a single, unique user
 * @param name The user's name, not null
 * @param currencySymbol The symbol used for the user's preferred currency ($, C$, ¥, £, etc.), not null
 * @param dateFormat The format in which dates will be printed and accepted; the first instances of Y, M, and D
 *                   will be replaced by the year, month, or day respectively. If a character is repeated
 *                   contiguously (such as MMM), it will be replaced with the given unit as an equal number of digits
 *                   (such as 031). Not null
 * @param isDecimalCurrency Whether the base unit of the currency is displayed with ($5.00) or without (¥500) a decimal point
 * @author Xavier Mercerweiss
 * @version v2.0 2025-12-30
 */
public record Profile(String name, String currencySymbol, String dateFormat, boolean isDecimalCurrency)
{
}
