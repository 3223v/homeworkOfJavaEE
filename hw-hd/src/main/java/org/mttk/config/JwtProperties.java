package org.mttk.config;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private String SecretKey="SecretKey123456789123456789123456789123456789123456789123456789123456789123456789123456789";
    private long Ttl=10*24*60*60*1000;
    private String TokenName="token";

}
