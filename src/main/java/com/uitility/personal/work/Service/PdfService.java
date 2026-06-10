package com.uitility.personal.work.Service;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;


import java.io.*;

@Service
public class PdfService {

    // Convert PDF (InputStream) -> simple HTML (string)
    public String pdfToHtml(InputStream pdfInput) throws IOException {
//        try (PDDocument doc = PDDocument.load(pdfInput)) {
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(doc);
//            // Simple line-splitting to paragraphs
//            StringBuilder html = new StringBuilder();
//            html.append("<!doctype html><html><head><meta charset='utf-8'><style>")
//                    .append("body{font-family: sans-serif; padding: 20px;} p{margin-bottom:10px;}")
//                    .append("</style></head><body>");
//            String[] lines = text.split("\\r?\\n");
//            boolean inPara = false;
//            StringBuilder para = new StringBuilder();
//            for (String line : lines) {
//                if (line.trim().isEmpty()) {
//                    if (para.length() > 0) {
//                        html.append("<p>").append(escapeHtml(para.toString().trim())).append("</p>");
//                        para.setLength(0);
//                    }
//                } else {
//                    if (para.length() > 0) para.append(" ");
//                    para.append(line.trim());
//                }
//            }
//            if (para.length() > 0) {
//                html.append("<p>").append(escapeHtml(para.toString().trim())).append("</p>");
//            }
//            html.append("</body></html>");
//            return html.toString();
//        }
//    }
//
//    // Convert HTML -> PDF (OutputStream)
//    public void htmlToPdf(String html, OutputStream out) throws IOException {
//        try {
//            PdfRendererBuilder builder = new PdfRendererBuilder();
//            builder.withHtmlContent(html, null);
//            builder.toStream(out);
//            builder.run();
//        } catch (Exception e) {
//            throw new IOException("HTML to PDF conversion failed", e);
//        }
//    }
//
//    private String escapeHtml(String s) {
//        return s.replace("&", "&amp;")
//                .replace("<", "&lt;")
//                .replace(">", "&gt;");
//    }
        return "sagar" ;
    }
}

