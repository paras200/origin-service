package com.ilab.origin.certificates.service;

import org.apache.commons.lang3.StringUtils;

import com.ilab.origin.certificates.model.Certificates;
import com.ilab.origin.tracker.error.OriginException;

public class CertificatesUtils {

	public static String getKeyInputForSignature(Certificates certificates) throws OriginException {
		StringBuilder sb = new StringBuilder();
		addToText(sb, certificates.getUniversityName());
		addToText(sb, certificates.getInstituteName());
		addToText(sb, certificates.getCourseName());
		addToText(sb, certificates.getStudentName());
		addToText(sb, certificates.getDateOfBirth());
		addToText(sb, certificates.getCertificateId());
		addToText(sb, certificates.getTimeinmilli()+"");
		return sb.toString();
	}
	
	private static void addToText(StringBuilder sb, String data) throws OriginException {
		if(StringUtils.isEmpty(data)) {
			 throw new OriginException("Signature can't be generated as key fields are blank , please ensure the follwing fields are not NULL."
			 		+ " universityName, instituteName, courseName , studentName, dateOfBirth , certificateId");
		}
		sb.append(data.trim().toLowerCase());
	}
}
