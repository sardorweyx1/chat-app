package uz.weyx.chatapp.service.minio;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Component
public class FileUploader {
    public static final String ACCESS_KEY = "5BXPmkOpjXXxH2PZ";
    public static final String SECRET_KEY = "JBoIzp2DyEgaFPhg1nhoWUOdCjG8Rees";
    public static final String ENDPOINT = "http://localhost:9000";
    public static final String BUCKET_NAME = "app-chat";

    private MinioClient minioClient;


    @PostConstruct
    public void init() {
        minioClient = MinioClient
                .builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }


    public void upload(String objectName, MultipartFile file) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            } else {
                System.out.printf("Bucket '%s' already exists.", BUCKET_NAME);
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName + "." + FileNameUtils.getExtension(file.getOriginalFilename()))
                            .stream(
                                    file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            System.out.printf("'%s' is successfully uploaded as "
                    + " to bucket '%s'.%n", file.getName(), BUCKET_NAME);
        } catch (MinioException minioException) {
            System.out.println(minioException.getMessage());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public String getTempUrl(String objectName) {
        try {
            String url =
                    minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(BUCKET_NAME)
                                    .object(objectName)
                                    .expiry(2, TimeUnit.HOURS)
                                    .build());
            System.out.println(url);
            return url;
        } catch (MinioException minioException) {
            System.out.println(minioException.getMessage());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }
}
