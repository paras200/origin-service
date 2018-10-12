package com.ilab.origin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ilab.origin.admin.processor.TestDataGenerator;
import com.ilab.origin.tracker.error.OriginException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminTest {

	@Autowired
	private TestDataGenerator testDataGen;

	@Test
	public void generateTestData() throws OriginException {
		testDataGen.generateTestData("LOT-A", 70, 15);
		
	}
}
