package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;
import com.epam.izh.rd.online.autcion.mappers.BidMapper;
import com.epam.izh.rd.online.autcion.mappers.ItemMapper;
import com.epam.izh.rd.online.autcion.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcTemplatePublicAuction implements PublicAuction {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BidMapper bidMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<Bid> getUserBids(long id) {
        return jdbcTemplate.query("SELECT * FROM BIDS WHERE USER_ID =?", bidMapper, id);
    }

    @Override
    public List<Item> getUserItems(long id) {
        return jdbcTemplate.query("SELECT * FROM items WHERE USER_ID=?", itemMapper, id);
    }

    @Override
    public Item getItemByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM ITEMS WHERE TITLE LIKE '%" + name + "%'", itemMapper);
    }

    @Override
    public Item getItemByDescription(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM ITEMS WHERE DESCRIPTION LIKE '%" + name + "%'", itemMapper);
    }

    @Override
    public Map<User, Double> getAvgItemCost() {
        List<Item> itemList = jdbcTemplate.query("SELECT * FROM ITEMS",itemMapper);
        List<User> userList = jdbcTemplate.query("SELECT * FROM USERS",userMapper);
        Map<User, Double> map = new HashMap<>();
        for (User user : userList){
            double startPrice = 0;
            int count = 0;
            for (Item item : itemList){
                if (user.getUserId().equals(item.getUserId())){
                    startPrice+=item.getStartPrice();
                    count++;
                }
            }
            if (count!= 0) {
                map.put(user, startPrice / count);
            }
        }
        return map;    }

    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {
        Map<Item, Bid> map = new HashMap<>();
        List<Item> itemList = jdbcTemplate.query("SELECT * FROM ITEMS",itemMapper);
        List<Bid> bidList = jdbcTemplate.query("SELECT * FROM BIDS",bidMapper);
        for (Bid bid : bidList){
            double bidVal = 0;
            for (Item item : itemList){
                if (bid.getBidValue()> bidVal && item.getItemId().equals(bid.getItemId())){
                    map.put(item,bid);
                }
            }
        }
        return map;
    }

    @Override
    public boolean createUser(User user) {
        int count = jdbcTemplate.update("INSERT INTO USERS VALUES ( ?,?,?,?,?)",
                user.getUserId(),
                user.getBillingAddress(),
                user.getFullName(),
                user.getLogin(),
                user.getPassword());

        return count != 0;
    }

    @Override
    public boolean createItem(Item item) {
        int count = jdbcTemplate.update("INSERT INTO ITEMS VALUES (?,?,?,?,?,?,?,?,?)",
                item.getItemId(),
                item.getBidIncrement(),
                item.getBuyItNow(),
                item.getDescription(),
                item.getStartDate(),
                item.getStartPrice(),
                item.getStopDate(),
                item.getTitle(),
                item.getUserId());

        return count != 0;
    }

    @Override
    public boolean createBid(Bid bid) {
        int count = jdbcTemplate.update("INSERT INTO bids VALUES (?,?,?,?,?)",
                bid.getBidId(),
                bid.getBidDate(),
                bid.getBidValue(),
                bid.getItemId(),
                bid.getUserId());

        return count != 0;
    }

    @Override
    public boolean deleteUserBids(long id) {
        int count = jdbcTemplate.update("DELETE FROM bids WHERE user_id = ?", id);
        return count != 0;
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        int count = jdbcTemplate.update("UPDATE ITEMS SET START_PRICE = START_PRICE*2 WHERE USER_ID=?",id);
        return count!=0;
    }
}
