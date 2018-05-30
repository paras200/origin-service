package com.ilab.origin.tracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilab.origin.common.model.LocationTO;
import com.ilab.origin.tracker.error.OriginException;
import com.ilab.origin.tracker.model.TrackingData;
import com.ilab.origin.tracker.repo.TrackingRepository;
import com.ilab.origin.usermgt.model.Location;
import com.ilab.origin.validator.model.Result;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path="/tracker")
public class TrackingService {
	
	@Autowired
	private TrackingRepository trackRepo;

	@PostMapping("/save-qrcode")	
	public TrackingData saveQRCode(@RequestBody TrackingData trackData){		
		System.out.println(" saving QR code for tracking :" + trackData);
		return trackRepo.save(trackData);
	}
	
	@PostMapping("/save-all-codes")	
	public Result saveAllQRCode(@RequestBody List<TrackingData> trackDataList){		
		System.out.println(" saving QR code for tracking :" + trackDataList);
		trackRepo.save(trackDataList);
		return new Result();
	}
	
	@PostMapping("/save-location")	
	public Result saveTrackingData(@RequestBody LocationTO location) throws OriginException{		
		System.out.println(" saving tracking location for qrcode: " + location.getQrCode());
		if(StringUtils.isEmpty(location.getQrCode()) || location.getLocation() == null)
		{
			throw new OriginException("qrcode or location is null");
		}
		
		TrackingData trackData = trackRepo.findByQrCode(location.getQrCode());
		if(trackData == null) {
			throw new OriginException("qrcode seems to be invalid, its not recognized by the system");
		}
		List<Location> locationList = trackData.getLocations();
		if(locationList == null) {
			System.out.println("this is the 1st call for location tracking for the qr : " + location.getQrCode());
			locationList = new ArrayList<>();
		}
		locationList.add(location.getLocation());
		trackData = trackRepo.save(trackData);
		System.out.println("tracking data is updated with location, updated record : " + trackData);
		return new Result();
	}
	
	@GetMapping("/get-by-qrcode")
	public TrackingData getTrackingDetails(@RequestParam(value="qrcode") String qrcode){
		return trackRepo.findByQrCode(qrcode);
	}
}