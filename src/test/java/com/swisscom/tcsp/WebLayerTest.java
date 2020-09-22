package com.swisscom.tcsp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TCSPApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Ignore
public abstract class WebLayerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
