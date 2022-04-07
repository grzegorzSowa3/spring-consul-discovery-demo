package pl.recompiled.springconsuldiscoverydemoclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloEndpoint {

    @Autowired
    private ServerClient serverClient;

    @Value("${spring.cloud.consul.discovery.instanceId}")
    private String instanceId;


    @GetMapping
    public ResponseEntity<Hello> hello() {
        return ResponseEntity.ok(
                new Hello(
                        String.format("Hello world from %s!!!", instanceId)
                ));
    }

    @GetMapping("extended")
    public ResponseEntity<Hello> helloExtended() {
        Hello helloFromServer = serverClient.getHello();
        return ResponseEntity.ok(new Hello(
                String.format("Hello world from %s, passed by %s!!!",
                        helloFromServer.getMessage(),
                        instanceId
                )));
    }

}
