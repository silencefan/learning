package com.to8to.learn.lock;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @Description: TODO
 * @author: felix.fan
 * @date: 2018/12/10 21:10
 * @version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class DistributedLockTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void select() {
        Map<String, Object> map = jdbcTemplate.queryForMap("SELECT * FROM methodLock");
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("test", "哈哈");
        System.out.println(redisTemplate.opsForValue().get("test"));
    }
}
