package com.norkadev.pdf_file_service.controller;

import com.norkadev.pdf_file_service.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: ADD INTEGRATION TESTS CASES FOR THIS CONTROLLER: https://www.arhohuttunen.com/spring-boot-webmvctest/
@WebMvcTest(FileController.class)
class FileControllerIntegrationTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @Test
    void testMergePdfs_BadRequest_WhenLessThanTwoFiles() throws Exception {
        MockMultipartFile file1 = createMockPdf("file1.pdf");
        MultipartFile[] files = {file1};
        when(fileService.mergePdfs(files)).thenThrow(new IllegalArgumentException("Need at least 2 files to merge."));

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files/merge")
                        .file(file1))
                .andExpect(status().isBadRequest());

        verify(fileService, times(1)).mergePdfs(files);
    }

    private MockMultipartFile createMockPdf(String filename) {
        return new MockMultipartFile("files", filename, "application/pdf", new byte[]{1, 2, 3});
    }
}