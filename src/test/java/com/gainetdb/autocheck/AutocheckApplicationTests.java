package com.gainetdb.autocheck;

import com.gainetdb.autocheck.service.AttendMachineService;
import com.gainetdb.autocheck.utils.MobileMessageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AutocheckApplicationTests {
	@Autowired
	private AttendMachineService attendMachineService;
	@Test
	public void contextLoads() {
	}

	@Autowired
	private MobileMessageUtils mobileMessageUtils;

	@Test
	public void send() {
		//mobileMessageUtils.sendMessage("15038059874","你好");
		//Assert.assertNotNull(result);
	}

	@Test
	public void attendAttendMachine() {
	 //	attendMachineService.attendMachineCheck();
	}

	@Test
	public void attendMachineCheckByUrl() {
		//attendMachineService.attendMachineCheckByUrl("2016101301");
	}

}
