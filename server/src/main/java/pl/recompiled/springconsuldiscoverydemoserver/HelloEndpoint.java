package pl.recompiled.springconsuldiscoverydemoserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloEndpoint {

    @Value("${spring.cloud.consul.discovery.instanceId}")
    private String instanceId;

    @GetMapping
    public ResponseEntity<Hello> hello() {
        return ResponseEntity.ok(
                new Hello(
                        String.format("Hello world from %s!!!", instanceId)
                ));
    }
}
