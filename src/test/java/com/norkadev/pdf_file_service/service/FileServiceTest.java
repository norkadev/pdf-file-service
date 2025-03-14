package com.norkadev.pdf_file_service.service;

import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mockito.MockitoAnnotations;
import org.apache.pdfbox.pdmodel.PDDocument;

class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMergePdfs_Success() throws IOException {
        MockMultipartFile file1 = createMockPdf("file1.pdf");
        MockMultipartFile file2 = createMockPdf("file2.pdf");

        MultipartFile[] files = {file1, file2};
        byte[] mergedPdf = fileService.mergePdfs(files);

        assertNotNull(mergedPdf);
        assertTrue(mergedPdf.length > 0);
    }

    @Test
    void testMergePdfs_ThrowsException_WhenLessThanTwoFiles() throws IOException {
        MockMultipartFile file1 = null;
        file1 = createMockPdf("file1.pdf");
        MultipartFile[] files = {file1};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fileService.mergePdfs(files));
        assertEquals("Need at least 2 files to merge.", exception.getMessage());
    }

    private MockMultipartFile createMockPdf(String filename) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            document.addPage(new PDPage());
            document.save(outputStream);
        }
        return new MockMultipartFile("files", filename, "application/pdf", outputStream.toByteArray());
    }

}