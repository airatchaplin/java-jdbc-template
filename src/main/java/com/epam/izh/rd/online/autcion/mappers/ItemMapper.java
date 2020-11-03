package com.epam.izh.rd.online.autcion.mappers;

import com.epam.izh.rd.online.autcion.entity.Item;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Item(resultSet.getLong(1),
                resultSet.getDouble(2),
                resultSet.getBoolean(3),
                resultSet.getString(4),
                resultSet.getDate(5).toLocalDate(),
                resultSet.getDouble(6),
                resultSet.getDate(7).toLocalDate(),
                resultSet.getString(8),
                resultSet.getLong(9));
    }
}
