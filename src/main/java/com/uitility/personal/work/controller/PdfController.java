package com.uitility.personal.work.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PdfController {

//    private final PdfService pdfService;
//    private final InMemoryStore store;
//
//    public PdfController(PdfService pdfService, InMemoryStore store) {
//        this.pdfService = pdfService;
//        this.store = store;
//    }
//
//    @PostMapping("/pdf/upload")
//    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) throws Exception {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of("error", "file empty"));
//        }
//        String html = pdfService.pdfToHtml(file.getInputStream());
//        String id = UUID.randomUUID().toString();
//        store.save(id, html);
//
//        Map<String, Object> resp = new HashMap<>();
//        resp.put("id", id);
//        resp.put("html", html); // you can return or just return id
//        return ResponseEntity.ok(resp);
//    }
//
//    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
//    public ResponseEntity<String> getHtml(@PathVariable String id) {
//        if (!store.exists(id)) return ResponseEntity.notFound().build();
//        String html = store.get(id);
//        return ResponseEntity.ok(html);
//    }
//
//    @PutMapping(value = "/html/{id}", consumes = MediaType.TEXT_HTML_VALUE)
//    public ResponseEntity<?> updateHtml(@PathVariable String id, @RequestBody String html) {
//        if (!store.exists(id)) return ResponseEntity.notFound().build();
//        store.save(id, html);
//        return ResponseEntity.ok(Map.of("id", id));
//    }
//
//    @GetMapping("/pdf/download/{id}")
//    public ResponseEntity<byte[]> downloadPdf(@PathVariable String id) throws Exception {
//        if (!store.exists(id)) return ResponseEntity.notFound().build();
//        String html = store.get(id);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        pdfService.htmlToPdf(html, out);
//        byte[] pdfBytes = out.toByteArray();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.pdf\"")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdfBytes);
//    }
}

