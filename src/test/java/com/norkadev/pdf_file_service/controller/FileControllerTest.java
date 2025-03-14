package com.norkadev.pdf_file_service.controller;

import com.norkadev.pdf_file_service.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;


class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMergePdfs_Success() throws IOException {
        MockMultipartFile file1 = createMockPdf("file1.pdf");
        MockMultipartFile file2 = createMockPdf("file2.pdf");

        MultipartFile[] files = {file1, file2};
        byte[] mergedPdf = new byte[]{1, 2, 3};

        when(fileService.mergePdfs(Mockito.any(MultipartFile[].class))).thenReturn(mergedPdf);

        ResponseEntity<byte[]> response = fileController.mergePdfs(files);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertArrayEquals(mergedPdf, response.getBody());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
    }

    private MockMultipartFile createMockPdf(String filename) {
        return new MockMultipartFile("files", filename, "application/pdf", new byte[]{1, 2, 3});
    }
}