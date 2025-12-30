package net.xmercerweiss.budgeteer2.data;


/**
 * Each {@code Budget} object aggregates access to a given user's {@link Profile} and
 * associated {@link Ledger} such that both may be referenced as a unit
 * @param profile A user's profile, not null
 * @param ledger A user's ledger, not null
 * @author Xavier Mercerweiss
 * @version v2.0 2025-12-30
 */
public record Budget(Profile profile, Ledger ledger)
{
}
