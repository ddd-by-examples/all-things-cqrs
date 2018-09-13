package io.dddbyexamples.cqrs.persistence;

import org.h2.api.Trigger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CreditCardUsedTrigger implements Trigger {

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {

    }

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO WITHDRAWAL (ID, CARD_ID, AMOUNT) " + "VALUES (?, ?, ?)")) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setObject(2, newRow[0]);
            stmt.setObject(3, ((BigDecimal) newRow[2]).subtract((BigDecimal) oldRow[2]));


            stmt.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {

    }
}
