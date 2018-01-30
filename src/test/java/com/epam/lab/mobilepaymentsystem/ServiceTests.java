package com.epam.lab.mobilepaymentsystem;


import com.epam.lab.mobilepaymentsystem.model.ServiceUnit;
import com.epam.lab.mobilepaymentsystem.service.ServiceUnitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MobilePaymentSystemApplication.class})
public class ServiceTests {

    @Autowired
    private ServiceUnitService serviceUnitService;

    private static int count = 0;

    private ServiceUnit createTestService() {
        ++count;
        ServiceUnit serviceUnit = new ServiceUnit();
        serviceUnit.setName("test service" + count);
        serviceUnit.setCost(100);
        serviceUnitService.save(serviceUnit);
        return serviceUnit;
    }

    @Test
    public void addService() {
        ServiceUnit serviceUnit = createTestService();
        String serviceName = serviceUnit.getName();
        assertEquals(serviceName,
                serviceUnitService.getByServiceName(serviceName).getName());
    }

    // todo: for some reason it fails
    @Test
    public void numberOfServices() {
        assertEquals(count, serviceUnitService.numberOfAllService());
    }

    @Test
    public void updateService() {
        ServiceUnit serviceUnit = createTestService();
        String newName = "new name";
        String description = "test description";

        serviceUnit.setName(newName);
        serviceUnit.setDescription(description);
        serviceUnitService.save(serviceUnit);
        assertEquals(description,
                serviceUnitService.getByServiceName(newName).getDescription());
    }
}
