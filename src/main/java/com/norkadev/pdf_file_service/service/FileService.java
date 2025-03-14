package com.norkadev.pdf_file_service.service;

import com.norkadev.pdf_file_service.model.File;
import com.norkadev.pdf_file_service.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public byte[] mergePdfs(MultipartFile[] files) throws IOException {
        log.info("mergePdfs {} files", files.length);
        if (files.length < 2) {
            throw new IllegalArgumentException("Need at least 2 files to merge.");
        }
        if (files.length > 5) {
            throw new IllegalArgumentException("Up to 5 files can be merged at once.");
        }

        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (MultipartFile file : files) {
            if (!"application/pdf".equals(file.getContentType())) {
                throw new IllegalArgumentException("All files must be PDFs.");
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Each file must be less than 10 MB.");
            }

            InputStream inputStream = file.getInputStream();
            merger.addSource(inputStream);
        }

        merger.setDestinationStream(outputStream);
        merger.mergeDocuments(null);

        return outputStream.toByteArray();
    }

    public File saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getSize() > 10485760 || !file.getContentType().equals("application/pdf")) { // 10 MB limit
            throw new IllegalArgumentException("File is empty or exceeds the size limit of 10 MB");
        }

        File fileentity = File.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .size(file.getSize())
                .data(file.getBytes())
                .build();
       return fileRepository.save(fileentity);
    }

    public List<File> saveFiles(MultipartFile[] files) throws IOException {
        if (files.length > 5) {
            throw new IllegalArgumentException("Cannot upload more than 5 files at once");
        }

        List<File> savedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty() || !"application/pdf".equals(file.getContentType()) || file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Error processing file: " + file.getOriginalFilename());
            }

            File fileEntity = File.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .size(file.getSize())
                    .data(file.getBytes())
                    .build();

            savedFiles.add(fileRepository.save(fileEntity));
        }
        return savedFiles;
    }

    public Optional<File> getFileById(Integer id) {
        return fileRepository.findById(id);
    }

    public void deleteFileById(Integer id) {
        fileRepository.deleteById(id);
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }
}
