package com.liang.bbs.rest.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
@Data
@Slf4j
public class FileLengthUtils {
    /**
     * 涓婁紶婧愭枃浠跺厑璁哥殑鏈€澶у€间笉寰楀ぇ浜巉ileLength
     */
    @Value("${file.source.length}")
    private long fileMaxLength;

    /**
     * 鏂囦欢鏄惁杩囧ぇ
     *
     * @param bytes
     * @return
     */
    public Boolean isFileNotTooBig(byte[] bytes) {
        // 褰撳墠鏂囦欢澶у皬
        long currentFileSize = bytes.length;
        if (currentFileSize <= fileMaxLength) {
            return true;
        } else {
            return false;
        }
    }

}
