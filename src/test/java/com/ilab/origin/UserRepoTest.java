package com.ilab.origin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.AuditRepository;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.usermgt.repo.UserRepository;
import com.ilab.origin.validator.model.ValidationData;
import com.ilab.origin.validator.repo.ValidatorRepo;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepoTest {

    @Autowired
    UserRepository repository;
    
    @Autowired
    MerchantRepository mrRepo;
    
    @Autowired
    AuditRepository auditRepo;
    
    @Autowired
    private ValidatorRepo vRepo;

    private User aks, nish;
    private Merchant maruti;
    private ValidationData vData;

    @Before
    public void setUp() {

       repository.deleteAll();
        aks = new User();
        aks.setLocation("India");
        aks.setUserId("ask");
        aks.setMobileNumber("999999999");
        aks = repository.save(aks);
        
        nish = new User();
        nish.setLocation("India");
        nish.setUserId("nisk");
        nish.setMobileNumber("777777777");
        
        nish = repository.save(nish);
        
        //mrRepo.deleteAll();
        maruti = new Merchant();
        maruti.setCompanyRegNumber("Maruti-Reg");
        maruti.setName("Maruti India Pvt Ltd");
        maruti.setMobileNumber("10101010110");
        mrRepo.save(maruti);
        
        vData = new ValidationData();
        vData.setId("123");
        vData.setQrKey("xxYYrsdsd");
        vData.setProductName("Swift");
        
        vData.setSold(false);
        vRepo.save(vData);
    }
    
    @Test
    public void findByUserId() {
    	//repository.deleteAll();
    	repository.findAll();
     /*   User user = repository.findByUserId("aks");
        UserAudit audit = new UserAudit();
        audit.setUserId(user.getUserId());
        
        auditRepo.save(audit);
        
        List<UserAudit> auditLog = auditRepo.findByUserId(user.getUserId());*/
        System.out.println("************** AUDIT LOG***** ");
       // assertThat(user.extracting("userId").contains("aks");
    }
}
