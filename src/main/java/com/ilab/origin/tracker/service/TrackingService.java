package com.ilab.origin.tracker.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.common.mongo.MongoQueryManager;
import com.ilab.origin.common.utils.ValidationUtils;
import com.ilab.origin.notification.fcm.FirebaseNotification;
import com.ilab.origin.serial.SerialNumberGenerator;
import com.ilab.origin.serial.TimestampSerialization;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.tracker.model.TrackingData;
import com.ilab.origin.tracker.model.TransactionInfo;
import com.ilab.origin.tracker.repo.TrackingDataRepository;
import com.ilab.origin.tracker.repo.TxInfoRepository;
import com.ilab.origin.validator.model.OriginStatus;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/tracker")
public class TrackingService {

	private static Log log = LogFactory.getLog(TrackingService.class.getName());
	
	@Autowired
	private TxInfoRepository tiRepo;
	
	@Autowired
	private TrackingDataRepository trackingDataRepo;
	
	@Autowired
	private ShipmentTrackRecorder shipmentTrackRecorder; 
	
	@Autowired
	private MongoQueryManager mongoQueryMgr;
	
	@Autowired
	private FirebaseNotification firebaseNotification; 
	
	private SerialNumberGenerator generator = TimestampSerialization.getInstance();

	@PostMapping("/save-txdata")	
	public TransactionInfo saveQRCode(@RequestBody TransactionInfo ti){		
		log.info(" saving QR code for tracking :" + ti);
		setLotNumberIfNull(ti);
		addQRCode(ti);
		return tiRepo.save(ti);
	}
	
	private void addQRCode(TransactionInfo ti) {
		// ti.setQrcode(ti.getNationalDrugCode() + "-"  +ti.getLotNumber() );
		ti.setQrcode(ti.getLotNumber());
	}

	@PostMapping("/save-all-txdata")	
	public List<TransactionInfo> saveAllQRCode(@RequestBody List<TransactionInfo> trackDataList){		
		log.info(" saving QR code for tracking :" + trackDataList);
		for (TransactionInfo txInfo : trackDataList) {
			setLotNumberIfNull(txInfo);
			addQRCode(txInfo);
		}
		trackDataList = tiRepo.save(trackDataList);
		return trackDataList;
	}

	private void setLotNumberIfNull(TransactionInfo txInfo) {
		if(txInfo.getLotNumber() == null) {
			txInfo.setLotNumber("LOT"+generator.getSequenceNumber());
		}
	}
	
	
	@RequestMapping(value="/get-serial-number" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<String> getSequenceNumber(@RequestParam(value="count") Integer count){
		SerialNumberGenerator generator = TimestampSerialization.getInstance();
		return generator.getSequenceNumber(count);
	}
	
	@RequestMapping(value="/get-lot-number" , method = { RequestMethod.GET, RequestMethod.POST })
	public String getSequenceNumber(){
		 generator = TimestampSerialization.getInstance();
		return generator.getSequenceNumber();
	}
	
	@PostMapping("/save-tracing-data")	
	public TransactionInfo saveTrackingData(@RequestBody TrackingData td) throws OriginException{		
		log.info(" saving tracking location for qrcode: " + td.getQrcode());
		if(StringUtils.isEmpty(td.getQrcode()))
		{
			//throw new OriginException("qrcode and location must be provided");
			TransactionInfo ti = new TransactionInfo();
			ti.setQrcode(td.getQrcode());
			ti.setStatusCode(OriginStatus.NO_SCAN);
			return ti;
		}
		
		if(td.getLocation() == null) {
			log.error("Location is null for tracking :" +td);
		}
		
//		TransactionInfo trackData = tiRepo.findByQrCode(td.getQrCode());
//		if(trackData == null) {
//			throw new OriginException("qrcode seems to be invalid, its not recognized by the system");
//		}
		
		td = trackingDataRepo.save(td);
		
		shipmentTrackRecorder.asyncUpdate(td);
		
		log.info("tracking data is updated with location, updated record : " + td);
		TransactionInfo ti = tiRepo.findByQrcode(td.getQrcode());
		if(ti == null) {
			ti = new TransactionInfo();
			ti.setQrcode(td.getQrcode());
			ti.setStatusCode(-1);
		}
		return ti;
	}
	
	@GetMapping("/get-by-qrcode")
	public TransactionInfo getTrackingDetails(@RequestParam(value="qrcode") String qrcode){
		return tiRepo.findByQrcode(qrcode);
	}
	
	@RequestMapping(value="/get-latest-txinfo" , method = { RequestMethod.GET, RequestMethod.POST })
	public TransactionInfo getTransactionInfo(@RequestParam(value="qrcode") String qrcode){
		TransactionInfo ti = tiRepo.findByQrcode(qrcode);
		if(ti != null) {
			// get latest owner of the product // oderby creation date  TODO
			List<TrackingData> tdList =  trackingDataRepo.findByQrcode(qrcode);
			int index = tdList.size() -1;
			if(index >= 0) {
				TrackingData td = tdList.get(index);
				ti.setPreviousOwner(td.getOwner());
			}
		}
		return ti;
	}
	
	@RequestMapping(value="/get-latest-shipment-scan" , method = { RequestMethod.GET, RequestMethod.POST })
	public List<TrackingData> getLatestShipmentScanData(@RequestParam(value="merchantId") String merchantId) throws OriginException{
		
		ValidationUtils.validateInputParam(merchantId);

		Criteria merchantCrt = Criteria.where("merchantId").is(merchantId);
		Criteria manuFacturerCriteria = Criteria.where("manufacturerId").is(merchantId);
		List<Criteria> cList = new ArrayList<>();
		cList.add(merchantCrt);
		cList.add(manuFacturerCriteria);
		
		Criteria criteria = mongoQueryMgr.createORCriteria(cList);
		Query query = mongoQueryMgr.createQuery(criteria);
		query.with(new Sort(Sort.Direction.DESC, "creationDate"));
		List<TrackingData>  result = (List<TrackingData>) mongoQueryMgr.executeQuery(TrackingData.class, query );
		
		return result;
	}
	
	@PostMapping("/subscribe-topic")
	public int subscribeToTopic(@RequestParam(value="registrationToken") List<String> registrationToken , @RequestParam(value="topicName") String topicName){
		return firebaseNotification.subscribeToTopic(registrationToken, topicName);
	}
	
	@PostMapping("/un-subscribe-topic")
	public int unsubscribeToTopic(@RequestParam(value="registrationToken") List<String> registrationToken , @RequestParam(value="topicName") String topicName){
		return firebaseNotification.unsubscribeToTopic(registrationToken, topicName);
	}
	
	@RequestMapping(value="/get-topic-name" , method = { RequestMethod.GET, RequestMethod.POST })
	public String getTopicName(@RequestParam(value="userId") String userId, @RequestParam(value="merchantId") String merchantId) throws OriginException{
		
		return firebaseNotification.getTopicName(merchantId);
	}
}
