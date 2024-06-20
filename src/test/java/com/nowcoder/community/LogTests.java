package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = CommunityApplication.class)
public class LogTests {

        private static final Logger logger = LoggerFactory.getLogger(LogTests.class);

        @Test
        public void test(){
                logger.debug("debug log");
                logger.info("info log");
                logger.warn("warn log");
                logger.error("error log");
        }
        @Test
        public void search() {
                int[] nums = {-1,0,3,5,9,12};
                int target = 13;
                int length = nums.length-1;
                int right = 0;
                int left = length;
                int i = 0;
                while(right<=left) {
                        i = (right+left)/2;
                        if (nums[i]>target){
                                left = i-1;
                        }else if(nums[i]<target){
                                right = i+1;
                        }else{
                                System.out.println(i);
                        }
                }
                System.out.println(-1);
        }

}
