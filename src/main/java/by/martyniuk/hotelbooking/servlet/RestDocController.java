package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.impl.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/certificate")
public class RestDocController {

    @Autowired
    private DocumentServiceImpl documentService;

    @GetMapping("/csvCertificate/{userId}")
    public ResponseEntity findCertificates(@PathVariable long userId) throws ServiceException {
        return ResponseEntity.ok(documentService.generateCSVCertificate(userId));
    }

    @GetMapping("/pdfCertificate/{id}")
    public ResponseEntity<Resource> pdfCertificate(@PathVariable long id) {
        Resource body = documentService.generatePdfCertificate(id);

        if (body != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "CertificatePDF.pdf" + "\"").body(body);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/xlsCertificate")
    public ResponseEntity<Resource> xlsCertificate() {
        Resource body = documentService.generateXLSCertificate();

        if (body != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "CertificateXLS.xls" + "\"").body(body);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
