package com.prac.onlinesql;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

/**
 * @Auther: Administrator
 * @Date: 2019-05-21 11:00
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DownloadAndUploadTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    /**
     * 在每次测试执行前构建mvc环境
     */
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void upload(){
        File file = new File("H:/Effective Java.pdf");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            String result =  mockMvc.perform(
                    MockMvcRequestBuilders
                            .fileUpload("/dbs/download")
                            .file(
                                    new MockMultipartFile("file", fis)
                            )
            ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn().getResponse().getContentAsString();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
