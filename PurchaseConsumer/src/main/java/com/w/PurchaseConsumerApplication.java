package com.w;

import com.alibaba.dubbo.container.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableHystrix
@SpringBootApplication(scanBasePackages = {"com.w.common","com.w.purchaseconsumer"})
public class PurchaseConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PurchaseConsumerApplication.class, args);
        // 启动 Provider 容器，注意这里的 Main 是 com.alibaba.dubbo.container 包下的
        Main.main(args);
    }
}
