package com.ilab.origin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ilab.origin.usermgt.model.Field;
import com.ilab.origin.usermgt.model.Merchant;
import com.ilab.origin.usermgt.model.QRTemplates;
import com.ilab.origin.usermgt.model.User;
import com.ilab.origin.usermgt.repo.AuditRepository;
import com.ilab.origin.usermgt.repo.MerchantRepository;
import com.ilab.origin.usermgt.repo.TemplateRepository;
import com.ilab.origin.usermgt.repo.UserRepository;
import com.ilab.origin.validator.model.OriginData;
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

    @Autowired
    private TemplateRepository templateRepo;
    
    private User aks, nish;
    private Merchant maruti;
    private OriginData vData;

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
        maruti.setMerchantKey("MRT");
        maruti.setCompanyRegNumber("Maruti-Reg");
        maruti.setName("Maruti India Pvt Ltd");
        maruti.setMobileNumber("10101010110");
        maruti = mrRepo.save(maruti);
        
        vData = new OriginData();
        vData.setId("123");
        vData.setQrCode("code123");
        vData.setProductName("Swift");
        
		Map<String, String> dataMap = new HashMap<>(); 
		dataMap.put("batchId", "b1");
		dataMap.put("unitName", "GGN");
		vData.setDataMap(dataMap);
        
        vData.setSold(false);
        vRepo.save(vData);
        
        templateRepo.deleteAll();
        QRTemplates templates = new QRTemplates();
        templates.setMerchantId("mer-123");//maruti.getId()
        templates.setName("mytemplate-1");
        List<Field> fields = new ArrayList<>();
        
        Field ff = new Field();
        ff.setType("SELECT");
        ff.setFieldName("ProductName");
        List<String> value = new ArrayList<>();
        value.add("Alto");
        value.add("Dezireeeee");
		ff.setValue(value);
        fields.add( ff);
        
        Field ff2 = new Field();
        ff2.setType("TEXT");
        ff2.setFieldName("BatchName");
        //ff2.setValue("");
        fields.add( ff2);
		templates.setFields(fields);
		
		QRTemplates qrTemp = templateRepo.save(templates);
		
		templateRepo.findByMerchantId("mer-123");
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