package com.Hieu2k3.course.controllers;

import com.Hieu2k3.course.dtos.events.CertificateCreationEvent;
import com.Hieu2k3.course.dtos.responses.ApiResponse;
import com.Hieu2k3.course.dtos.responses.certificate.CertificateResponse;
import com.Hieu2k3.course.services.CertificateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/certificate")
@Slf4j
public class CertificateController {
    CertificateService certificateService;

    @PostMapping("")
    ApiResponse<Void> createCertification (@RequestBody CertificateCreationEvent request) {
        certificateService.createCertificate(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Certificate created")
                .build();
    }

    @GetMapping("")
    ApiResponse<List<CertificateResponse>> getCertificationByUserLogin() {
        return ApiResponse.<List<CertificateResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(certificateService.getCertificationByUser())
                .build();
    }
}
