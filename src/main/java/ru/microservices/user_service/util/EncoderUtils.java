package ru.microservices.user_service.util;


import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.experimental.UtilityClass;


@UtilityClass
public class EncoderUtils {


    public boolean verify(String password, String hashPassword) {
        return BCrypt
                .verifyer()
                .verify(
                        password.toCharArray(),
                        hashPassword.toCharArray()
                )
                .verified;
    }

    public String encode(String password) {
        return BCrypt
                .withDefaults()
                .hashToString(10, password.toCharArray());
    }
}
