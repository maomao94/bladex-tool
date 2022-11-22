package org.springblade.core.api.crypto.annotation.decrypt;

import org.springblade.core.api.crypto.enums.CryptoType;

import java.lang.annotation.*;

/**
 * rsa 解密
 *
 * @author licoy.cn
 * @see ApiDecrypt
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiDecrypt(CryptoType.RSA)
public @interface ApiDecryptRsa {
}
