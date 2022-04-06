package pl.recompiled.springconsuldiscoverydemoclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloEndpoint {

    @Autowired
    private ServerClient serverClient;

    @GetMapping
    public ResponseEntity<Hello> hello() {
        return ResponseEntity.ok(new Hello("Hello world from client app!!!"));
    }

    @GetMapping("extended")
    public ResponseEntity<Hello> helloExtended() {
        Hello helloFromServer = serverClient.getHello();
        return ResponseEntity.ok(new Hello(String.format("Hello world from %s, passed by client app!!!", helloFromServer.getMessage())));
    }

}
