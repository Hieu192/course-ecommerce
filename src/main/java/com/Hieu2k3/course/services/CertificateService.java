package com.Hieu2k3.course.services;

import com.Hieu2k3.course.dtos.events.CertificateCreationEvent;
import com.Hieu2k3.course.dtos.responses.certificate.CertificateResponse;

import java.util.List;

public interface CertificateService {
    List<CertificateResponse> getCertificationByUser();

    void createCertificate(CertificateCreationEvent event);
}
