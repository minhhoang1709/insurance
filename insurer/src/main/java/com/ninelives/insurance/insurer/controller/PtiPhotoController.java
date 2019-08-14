package com.ninelives.insurance.insurer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.insurer.service.InsurerPhotoService;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.util.Sha1EncryptionUtil;

@Controller
@RequestMapping("/insurer")
public class PtiPhotoController {

	private static final Logger logger = LoggerFactory.getLogger(PtiPhotoController.class);
	@Autowired
	InsurerPhotoService insurerPhotoService;
	@Autowired
	StorageProvider storageService;
	@Autowired
	Sha1EncryptionUtil sha1EncryptionUtil;

	public final String secretCode = "cbf01306c1865742901ff2e184020c60";

	@RequestMapping(value = "/pti/idtFiles/{orderId}/{hashString}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> downloadIdtPhotoFile(@PathVariable("orderId") String orderId,
			@PathVariable("hashString") String hashString) throws AppException {

		checkHashString(orderId, hashString);
		UserFile userFile = insurerPhotoService.fetchIdtPhotoFile(orderId);

		try {
			Resource file = storageService.loadAsResource(userFile.getFilePath());

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + userFile.getFileId() + "."
									+ userFile.getContentType()
											.substring(userFile.getContentType().lastIndexOf("/") + 1)
									+ "\"")
					.header(HttpHeaders.CONTENT_TYPE, userFile.getContentType()).body(file);
		} catch (StorageException e) {
			logger.error("Error on download photo", e);
			throw new AppInternalServerErrorException(ErrorCode.ERR1002_STORAGE_ERROR,
					"Maaf permintaan Anda belum dapat dilayani, terjadi kesalahan pada sistem");
		}
	}

	// claim photo files download controller
	@RequestMapping(value = "/pti/claimFiles/{claimId}/{claimDocTypeId}/{hashString}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> downloadClaimPhotoFiles(@PathVariable("claimId") String claimId,
			@PathVariable("claimDocTypeId") String claimDocTypeId, @PathVariable("hashString") String hashString)
			throws AppException {

		checkHashString(claimId, claimDocTypeId, hashString);
		UserFile userFile = insurerPhotoService.fetchClaimPhotoFile(claimId, claimDocTypeId);

		try {
			Resource file = storageService.loadAsResource(userFile.getFilePath());

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + userFile.getFileId() + "."
									+ userFile.getContentType()
											.substring(userFile.getContentType().lastIndexOf("/") + 1)
									+ "\"")
					.header(HttpHeaders.CONTENT_TYPE, userFile.getContentType()).body(file);
		} catch (StorageException e) {
			logger.error("Error on download photo", e);
			throw new AppInternalServerErrorException(ErrorCode.ERR1002_STORAGE_ERROR,
					"Maaf permintaan Anda belum dapat dilayani, terjadi kesalahan pada sistem");
		}
	}

	private void checkHashString(String claimId, String claimDocTypeId, String hashString)
			throws AppBadRequestException {
		String hashResult = sha1EncryptionUtil.sha1Encrypt(claimId, claimDocTypeId, secretCode);
		if (!hashResult.equals(hashString)) {
			throw new AppBadRequestException(ErrorCode.ERR1001_GENERIC_ERROR, "Invalid hashString");
		}
	}

	private void checkHashString(String orderId, String hashString) throws AppBadRequestException {
		String hashResult = sha1EncryptionUtil.sha1Encrypt(orderId, secretCode);
		if (!hashResult.equals(hashString)) {
			throw new AppBadRequestException(ErrorCode.ERR1001_GENERIC_ERROR, "Invalid hashString");
		}
	}
}
