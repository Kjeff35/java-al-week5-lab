package org.bexos.exercise_3.service;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bexos.exercise_3.util.BouncyCastleCryptoUtil;
import org.springframework.beans.factory.annotation.Value;

@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    @Value("${app.secret.key}")
    private String secretKey;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            return BouncyCastleCryptoUtil.encrypt(attribute, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return BouncyCastleCryptoUtil.decrypt(dbData, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
