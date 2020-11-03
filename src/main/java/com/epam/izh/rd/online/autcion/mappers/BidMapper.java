package com.epam.izh.rd.online.autcion.mappers;

import com.epam.izh.rd.online.autcion.entity.Bid;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BidMapper implements RowMapper<Bid> {

    @Override
    public Bid mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Bid(resultSet.getLong(1),
                resultSet.getDate(2).toLocalDate(),
                resultSet.getDouble(3),
                resultSet.getLong(4),
                resultSet.getLong(5));
    }
}
