package com.example.ohmobackend.service.fileService;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.S3Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileUploadService implements FileUploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 삭제
    public void delete(String fileName) {
        String[] urlParts = fileName.split("/");
        String fileBucket = urlParts[2].split("\\.")[0];

        if (!fileBucket.equals(bucket)) {
            throw new S3Handler(ErrorStatus.FILE_NOT_FOUND);
        }

        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));

        if (!amazonS3.doesObjectExist(bucket, objectKey)) {
            throw new S3Handler(ErrorStatus.FILE_NOT_FOUND);
        }

        try {
            amazonS3.deleteObject(bucket, objectKey);
        } catch (AmazonS3Exception e) {
            log.error("File delete fail : " + e.getMessage());
            throw new S3Handler(ErrorStatus.FILE_DELETE_FAILED);
        } catch (SdkClientException e) {
            log.error("AWS SDK client error : " + e.getMessage());
            throw new S3Handler(ErrorStatus.FILE_DELETE_FAILED);
        }

        log.info("File delete complete: " + objectKey);
    }

    @Override
    public String upload(MultipartFile file, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            log.error("Amazon S3 error while uploading file: " + e.getMessage());
            throw new S3Handler(ErrorStatus.FILE_UPLOAD_FAILED);
        } catch (SdkClientException e) {
            log.error("AWS SDK client error while uploading file: " + e.getMessage());
            throw new S3Handler(ErrorStatus.FILE_UPLOAD_FAILED);
        } catch (IOException e) {
            log.error("IO error while uploading file: " + e.getMessage());
            throw new S3Handler(ErrorStatus.FILE_UPLOAD_FAILED);
        }

        log.info("File upload completed: " + fileName);

        return amazonS3.getUrl(bucket, fileName).toString();
    }

}

