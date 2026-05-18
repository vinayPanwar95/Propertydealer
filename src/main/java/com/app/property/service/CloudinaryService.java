package com.app.property.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // IMAGE UPLOAD
    public String uploadImage(MultipartFile file) throws IOException {

        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "image")
        );

        return result.get("secure_url").toString();
    }

    // VIDEO UPLOAD
    public String uploadVideo(MultipartFile file) throws IOException {

        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "video")
        );

        return result.get("secure_url").toString();
    }

    public String deleteMedia(String publicId, String resourceType) throws Exception {

        Map result = cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.asMap("resource_type", resourceType)
        );

        return result.get("result").toString();
    }

    // DOWNLOAD MEDIA
    public MultipartFile[] downloadMedia(String fileUrl) throws IOException {
        java.net.URL url = new java.net.URL(fileUrl);
        java.net.URLConnection connection = url.openConnection();

        try (java.io.InputStream is = connection.getInputStream();
             java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] fileBytes = baos.toByteArray();
            String filename = extractFilenameFromUrl(fileUrl);
            String contentType = connection.getContentType();

            MultipartFile multipartFile = new InMemoryMultipartFile(
                    filename,
                    filename,
                    contentType != null ? contentType : "application/octet-stream",
                    fileBytes
            );

            return new MultipartFile[]{multipartFile};
        }
    }

    // Helper method to extract filename from URL
    private String extractFilenameFromUrl(String fileUrl) {
        try {
            String[] parts = fileUrl.split("/");
            return parts.length > 0 ? parts[parts.length - 1] : "download";
        } catch (Exception e) {
            return "download";
        }
    }

    // IN-MEMORY MULTIPART FILE IMPLEMENTATION
    public static class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] bytes;

        public InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] bytes) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.bytes = bytes;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return bytes == null || bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes != null ? bytes.length : 0;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return bytes;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(bytes);
            }
        }
    }
}
