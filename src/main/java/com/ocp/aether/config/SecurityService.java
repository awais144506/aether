package com.ocp.aether.config;

import com.ocp.aether.utility.SecurityUtility;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SecurityService {
    private byte[] masterKey;
    @PostConstruct
    public void init() throws Exception {
        String encodedKey = System.getenv("MASTER_ENCRYPTION_KEY");
        if(encodedKey==null)
        {
            throw new Exception("KEY DIDNT FOUND SORRY::::");
        }
        this.masterKey = Base64.getDecoder().decode(encodedKey);
    }
    public String encrypt(String data) throws Exception {
        return SecurityUtility.encrypt(data, this.masterKey);
    }

    public String decrypt(String encryptedData) throws Exception {
        return SecurityUtility.decrypt(encryptedData, this.masterKey);
    }
}
