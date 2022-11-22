package org.springblade.core.api.crypto.annotation.crypto;

import org.springblade.core.api.crypto.annotation.decrypt.ApiDecrypt;
import org.springblade.core.api.crypto.annotation.encrypt.ApiEncrypt;
import org.springblade.core.api.crypto.enums.CryptoType;

import java.lang.annotation.*;

/**
 * <p>AES加密解密含有{@link org.springframework.web.bind.annotation.RequestBody}注解的参数请求数据</p>
 *
 * @author Chill
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ApiEncrypt(CryptoType.AES)
@ApiDecrypt(CryptoType.AES)
public @interface ApiCrypto {

}
