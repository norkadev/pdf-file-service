package com.norkadev.pdf_file_service.controller;

import com.norkadev.pdf_file_service.model.File;
import com.norkadev.pdf_file_service.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "File Management", description = "Operations related to file management")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Merge multiple PDFs", description = "Merges multiple PDF files into one")
    @PostMapping(path = "/merge", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> mergePdfs(@RequestPart(required = true) MultipartFile[] files) throws IOException, IllegalArgumentException {
        log.info("POST /api/v1/files/merge {} files", files.length);

            byte[] mergedPdf = fileService.mergePdfs(files);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=merged-file.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(mergedPdf);

    }

    @Operation(summary = "Upload a file", description = "Uploads a file and returns its ID")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            File savedFile = fileService.saveFile(file);
            return ResponseEntity.ok("File successfully uploaded with ID: " + savedFile.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    @Operation(summary = "Upload multiple files", description = "Uploads multiple files up to 5 and returns their IDs")
    @PostMapping(path = "/bulk-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            List<File> savedFiles = fileService.saveFiles(files);
            List<String> fileIds = savedFiles.stream()
                    .map(file -> "File ID: " + file.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fileIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the files");
        }
    }

    @Operation(summary = "Download a PDF by ID", description = "Downloads a PDF file by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Integer id) {
        log.info("GET /api/v1/files/{} ", id);
        Optional<File> fileEntity = fileService.getFileById(id);

        if (fileEntity.isPresent()) {
            File file = fileEntity.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(file.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all files", description = "Retrieves a list of details of all uploaded files")
    @GetMapping
    public ResponseEntity<List<String>> getAllFiles() {
        log.info("GET /api/v1/files/ ");
        List<File> files = fileService.getAllFiles();
        if (files.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<String> fileInfo = files.stream()
                .map(file -> "ID: " + file.getId() + ", Nombre: " + file.getName() + ", Tamaño: " + file.getSize() + " bytes")
                .collect(Collectors.toList());
        return ResponseEntity.ok(fileInfo);
    }
}
