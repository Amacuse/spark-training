package com.griddynamics.dao.impl;

import com.datastax.driver.core.Session;
import com.griddynamics.dao.Spark4Product;
import com.griddynamics.dao.Spark4Upc;
import com.griddynamics.dao.config.Application;
import com.griddynamics.dao.model.Product;
import com.griddynamics.dao.model.Upc;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class Spark4ProductImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private Spark4Product spark4Product;
    @Autowired
    private Spark4Upc spark4Upc;
    @Autowired
    private Session session;

    private static final int PRODUCT_CAPACITY = 100;
    private static final int ACTIVE_PRODUCT_CAPACITY = 25;
    private static final int UPC_CAPACITY = 200;
    private static final int ACTIVE_UPC_CAPACITY = 50;

    @BeforeClass
    public void setUp() throws Exception {
        List<Product> products = new ArrayList<>(PRODUCT_CAPACITY);
        List<Upc> upcs = new ArrayList<>(UPC_CAPACITY);
        int activeUpcIndex = UPC_CAPACITY / ACTIVE_UPC_CAPACITY;
        int activeProductIndex = PRODUCT_CAPACITY / ACTIVE_PRODUCT_CAPACITY;

        for (int i = 1; i <= UPC_CAPACITY; i++) {
            Upc upc = new Upc(
                    i,
                    RandomStringUtils.randomAlphabetic(10),
                    RandomUtils.nextInt(1, 100));

            if (i % activeUpcIndex == 0) {
                upc.setStatusCode(1);
            } else {
                upc.setStatusCode(0);
            }

            upcs.add(upc);
        }

        for (int i = 1, j = 1; i <= PRODUCT_CAPACITY; i++) {
            Product product = new Product(
                    i,
                    RandomStringUtils.randomAlphabetic(10),
                    Arrays.asList(j++, j++));

            if (i % activeProductIndex == 0) {
                product.setStatusCode(1);
            } else {
                product.setStatusCode(0);
            }

            products.add(product);
        }

        spark4Product.addAll(products);
        spark4Upc.addAll(upcs);
    }

    @DataProvider(name = "getAllActiveAnswers")
    public Object[][] getActiveProducts() {
        List<Integer> activePids = new ArrayList<>(ACTIVE_PRODUCT_CAPACITY);
        int activeProductIndex = PRODUCT_CAPACITY / ACTIVE_PRODUCT_CAPACITY;

        for (int i = activeProductIndex; i <= PRODUCT_CAPACITY; i += activeProductIndex) {
            activePids.add(i);
        }

        return new Object[][]{{activePids}};
    }

    @Test(priority = 1, dataProvider = "getAllActiveAnswers")
    public void testGetAllActiveProducts(List<Integer> activePids) throws Exception {
        List<Integer> allActiveProductsPids = spark4Product.getPidOfActiveProducts();

        assertNotNull(allActiveProductsPids);
        assertEquals(allActiveProductsPids.size(), ACTIVE_PRODUCT_CAPACITY);
        assertTrue(allActiveProductsPids.containsAll(activePids));
    }

    @Test(priority = 2)
    public void testGetUpcsOfActiveProducts2() throws Exception {
        List<Integer> activeUid = spark4Product.getUidOfActiveUpcOfActiveProducts();

        assertNotNull(activeUid);
        assertEquals(activeUid.size(), ACTIVE_UPC_CAPACITY / 2);
    }

    @AfterClass
    public void tearDown() throws Exception {
        session.execute("TRUNCATE TABLE product");
        session.execute("TRUNCATE TABLE upc");
    }
}