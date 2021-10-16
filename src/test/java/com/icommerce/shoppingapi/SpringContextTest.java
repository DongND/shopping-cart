package com.icommerce.shoppingapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icommerce.shoppingapi.EcommerceApplication;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EcommerceApplication.class)
public class SpringContextTest {

    @Test
    public void contextLoads() {
    }

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}
