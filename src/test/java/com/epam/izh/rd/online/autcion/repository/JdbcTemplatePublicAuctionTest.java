package com.epam.izh.rd.online.autcion.repository;


import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ComponentScan(basePackages = "com.epam.izh.rd.online.autcion")
class JdbcTemplatePublicAuctionTest {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static Bid bid1;
    private static Bid bid2;
    private static Bid bid3;
    private static Bid bid4;
    private static Item item1;
    private static Item item2;
    private static Item item4;
    private static User user1;
    private static User user2;
    private static User user4;

    @Autowired
    private PublicAuction publicAuction;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RowMapper<Bid> bidRowMapper;
    @Autowired
    private RowMapper<Item> itemRowMapper;
    @Autowired
    private RowMapper<User> userRowMapper;

    @BeforeEach
    private void setup() throws ParseException {
        bid1 = new Bid(1L, transformDate("31.12.2004"), 10.0, 1L, 3L);
        bid2 = new Bid(2L, transformDate("31.12.2004"), 20.0, 1L, 2L);
        bid3 = new Bid(3L, transformDate("31.12.2004"), 30.0, 2L, 3L);
        bid4 = new Bid(4L, transformDate("31.12.2004"), 10.0, 1L, 4L);

        item1 = new Item(1L, 1.0, true, "description1", transformDate("31.12.2004"),
                50.0, transformDate("31.12.2004"), "title1", 1L);

        item2 = new Item(2L, 2.0, false,
                "description2", transformDate("31.12.2004"), 100.0,
                transformDate("31.12.2004"), "title2", 1L);
        item4 = new Item(4L, 1.0, false,
                "description4", transformDate("31.12.2004"), 100.0,
                transformDate("31.12.2004"), "title4", 3L);

        user1 = new User(1L, "address1", "VASYA1", "VASYALogin1", "VASYAPass1");
        user2 = new User(2L, "address2", "VASYA2", "VASYALogin2", "VASYAPass2");
        user4 = new User(4L, "address1", "VASYA1", "VASYALogin1", "VASYAPass1");
    }

    @AfterEach
    private void cleanup() {
        jdbcTemplate.execute("DELETE FROM bids WHERE bid_id=4");
        jdbcTemplate.execute("DELETE FROM items WHERE item_id=4");
        jdbcTemplate.execute("DELETE FROM users WHERE user_id=4");
    }

    @Test
    @DisplayName("Тест метода - Список ставок данного пользователя")
    void getUserBids() {
        long userId = 3;
        List<Bid> userBids = Lists.newArrayList(bid1, bid3);

        List<Bid> items = publicAuction.getUserBids(userId);

        assertIterableEquals(userBids, items);
    }

    @Test
    @DisplayName("Тест метода - Список лотов данного пользователя")
    void getUserItems() {
        long userId = 1;
        List<Item> userBids = Lists.newArrayList(item1, item2);

        List<Item> items = publicAuction.getUserItems(userId);

        assertIterableEquals(userBids, items);
    }

    @Test
    @DisplayName("Тест метода - Поиск лотов по подстроке в названии")
    void getItemByName() {
        String name = "title1";

        Item itemByName = publicAuction.getItemByName(name);

        assertEquals(itemByName, item1);
    }

    @Test
    @DisplayName("Тест метода - Поиск лотов по подстроке в описании")
    void getItemByDescription() {
        String description = "description1";

        Item itemByName = publicAuction.getItemByDescription(description);

        assertEquals(itemByName, item1);
    }

    @Test
    @DisplayName("Тест метода - Средняя цена лотов каждого пользователя")
    void getAvgItemCost() {
        Map<User, Double> userAndAvgItemCost = new HashMap<>();
        userAndAvgItemCost.put(
                user1,
                75.0
        );
        userAndAvgItemCost.put(
                user2,
                120.0
        );
        Map<User, Double> avgItemCost = publicAuction.getAvgItemCost();

        assertEquals(userAndAvgItemCost, avgItemCost);
    }

    @Test
    @DisplayName("Тест метода - Максимальный размер имеющихся ставок на каждый лот")
    void getMaxBidsForEveryItem() {
        Map<Item, Bid> itemsAndBids = new HashMap<>();
        itemsAndBids.put(
                item1,
                bid2
        );
        itemsAndBids.put(
                item2,
                bid3
        );
        Map<Item, Bid> maxBidsForEveryItem = publicAuction.getMaxBidsForEveryItem();

        assertEquals(itemsAndBids, maxBidsForEveryItem);
    }

    @DisplayName("Тест метода - Список действующих лотов данного пользователя")
    void getUserActualBids() {
        List<Bid> userActualBids = publicAuction.getUserActualBids(user1.getUserId());

        assertIterableEquals(singletonList(bid3), userActualBids);
    }

    @Test
    @DisplayName("Тест метода - Добавить нового пользователя")
    void createUser() {
        assertTrue(publicAuction.createUser(user4));

        User dbItem = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id=?", userRowMapper, 4);

        assertNotNull(dbItem);
        assertEquals(dbItem.getUserId(), user4.getUserId());
    }

    @Test
    @DisplayName("Тест метода - Добавить новый лот")
    void createItem() {
        assertTrue(publicAuction.createItem(item4));

        Item dbItem = jdbcTemplate.queryForObject("SELECT * FROM items WHERE item_id=?", itemRowMapper, 4);

        assertNotNull(dbItem);
        assertEquals(dbItem.getItemId(), item4.getItemId());
        assertEquals(dbItem.getStartDate(), item4.getStartDate());
    }

    @Test
    @DisplayName("Тест метода - Удалить ставки данного пользователя")
    void createBid() {
        assertTrue(publicAuction.createUser(user4));
        assertTrue(publicAuction.createBid(bid4));

        Bid dbItem = jdbcTemplate.queryForObject("SELECT * FROM bids WHERE bid_id=?", bidRowMapper, 4);

        assertNotNull(dbItem);
        assertEquals(dbItem.getBidId(), bid4.getBidId());
        assertEquals(dbItem.getBidDate(), bid4.getBidDate());
    }

    @Test
    @DisplayName("Тест метода - Удалить лоты данного пользователя")
    void deleteUserBids() {
        publicAuction.createUser(user4);
        publicAuction.createBid(bid4);

        assertTrue(publicAuction.deleteUserBids(user4.getUserId()));
        assertThrows(EmptyResultDataAccessException.class, () -> {
            jdbcTemplate.queryForObject("SELECT * FROM bids WHERE bid_id=?", bidRowMapper, 4);
        });
    }


    @Test
    @DisplayName("Тест метода - Увеличить стартовые цены товаров данного пользователя вдвое")
    void doubleItemsStartPrice() {
        long userId = 3;
        publicAuction.createItem(item4);

        assertTrue(publicAuction.doubleItemsStartPrice(userId));
        Item dbItem = jdbcTemplate.queryForObject("SELECT * FROM items WHERE item_id=?", itemRowMapper, 4);

        assertNotNull(dbItem);
        assertEquals(item4.getStartPrice() * 2, dbItem.getStartPrice(), 0.1);
    }

    @SneakyThrows
    private LocalDate transformDate(String string) throws ParseException {
        return new Date(DATE_FORMAT.parse(string).getTime()).toLocalDate();
    }
}