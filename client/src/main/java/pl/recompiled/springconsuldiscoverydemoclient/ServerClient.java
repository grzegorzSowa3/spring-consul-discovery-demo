package pl.recompiled.springconsuldiscoverydemoclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("server")
public interface ServerClient {

    @GetMapping("hello")
    Hello getHello();
}
