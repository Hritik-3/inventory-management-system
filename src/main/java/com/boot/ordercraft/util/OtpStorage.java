package com.boot.ordercraft.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStorage {

    private static class OtpData {
        String otp;
        LocalDateTime expiry;
        boolean verified;

        OtpData(String otp) {
            this.otp = otp;
            this.expiry = LocalDateTime.now().plusMinutes(5); // OTP valid for 5 minutes
            this.verified = false;
        }
    }

    private final ConcurrentHashMap<String, OtpData> otpMap = new ConcurrentHashMap<>();

    public void storeOtp(String email, String otp) {
        otpMap.put(email, new OtpData(otp));
    }

    public boolean isValidOtp(String email, String otp) {
        OtpData data = otpMap.get(email);
        if (data == null || LocalDateTime.now().isAfter(data.expiry)) return false;

        boolean match = data.otp.equals(otp);
        if (match) data.verified = true;
        return match;
    }

    public boolean isOtpVerified(String email) {
        OtpData data = otpMap.get(email);
        return data != null && data.verified && LocalDateTime.now().isBefore(data.expiry);
    }

    public void clearOtp(String email) {
        otpMap.remove(email);
    }
}
