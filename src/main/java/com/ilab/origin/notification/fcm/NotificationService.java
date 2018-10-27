package com.ilab.origin.notification.fcm;

import com.ilab.origin.tracker.model.TrackingData;

public interface NotificationService {

	public void sendTrackingUpdate(TrackingData trackingData) ;
}
