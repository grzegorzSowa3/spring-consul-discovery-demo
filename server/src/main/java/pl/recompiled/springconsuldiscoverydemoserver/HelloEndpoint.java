package pl.recompiled.springconsuldiscoverydemoserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloEndpoint {

    @GetMapping
    public ResponseEntity<Hello> hello() {
        return ResponseEntity.ok(new Hello("Hello world from server app!!!"));
    }
}
