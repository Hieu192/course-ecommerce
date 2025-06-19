package com.Hieu2k3.course.services.impl;

import com.Hieu2k3.course.dtos.events.CertificateCreationEvent;
import com.Hieu2k3.course.dtos.responses.certificate.CertificateResponse;
import com.Hieu2k3.course.services.CertificateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CertificateServiceImpl implements CertificateService {
    @Override
    public List<CertificateResponse> getCertificationByUser() {
        return List.of();
    }

    @Override
    public void createCertificate(CertificateCreationEvent event) {

    }
}
