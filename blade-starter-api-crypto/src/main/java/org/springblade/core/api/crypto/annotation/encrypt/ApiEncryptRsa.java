package org.springblade.core.api.crypto.annotation.encrypt;

import org.springblade.core.api.crypto.enums.CryptoType;

import java.lang.annotation.*;

/**
 * rsa 加密
 *
 * @author licoy.cn, L.cm
 * @see ApiEncrypt
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiEncrypt(CryptoType.RSA)
public @interface ApiEncryptRsa {
}
