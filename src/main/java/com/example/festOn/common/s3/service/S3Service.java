package com.example.festOn.common.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {

    private final AmazonS3 amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.default-profile-img}")
    private String defaultImg;

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    //NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) history/images
    public String uploadFile(MultipartFile multipartFile, String filePath) {

        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {

            String keyName = filePath + "/" + uploadFileName;

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            // AWS S3 업로드한 파일 URL 생성
            uploadFileUrl = amazonS3Client.getUrl(bucket, keyName).toString();

        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("파일 업로드 실패");
        }

        return uploadFileUrl; // 업로드된 파일의 S3 URL 반환
    }

    // 이미지 수정으로 인해 기존 이미지 삭제 메소드
    public void deleteImage(String ImgUrl) {
        String splitStr = ".amazonaws.com/";
        String fileName = ImgUrl.substring(ImgUrl.lastIndexOf(splitStr) + splitStr.length());

        // 기본 프로필 이미지가 아닐 때, 삭제
        if (!ImgUrl.equals(defaultImg)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            log.info("이미지 삭제 완료: {}", fileName);
        }
    }

    // 게시글 삭제 시, 옵션이미지 삭제
    public void deleteImg(String ImgUrl) {
        String splitStr = ".amazonaws.com/";
        String fileName = ImgUrl.substring(ImgUrl.lastIndexOf(splitStr) + splitStr.length());

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        log.info("이미지 삭제 완료: {}", fileName);
    }
}