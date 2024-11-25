package com.budgetMicroservice.util;

public class InvoiceUtils {

    public static String getFileExtensionFromContentType(String contentType) {
        return switch (contentType) {
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> ".xlsx";
            case "application/pdf" -> ".pdf";
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "application/zip" -> ".zip";
            case "text/plain" -> ".txt";
            default -> "";
        };
    }
}