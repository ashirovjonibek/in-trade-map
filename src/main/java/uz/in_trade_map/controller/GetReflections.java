package uz.in_trade_map.controller;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/ref")
public class GetReflections {

    @GetMapping
    public HttpEntity<?> get() {
        Reflections reflections = new Reflections("uz.in_trade_map");

        Set<Method> resources =
                reflections.get(Scanners.MethodsAnnotated.with(JpaRepository.class).as(Method.class));

        Set<Class<?>> singletons =
                reflections.get(Scanners.TypesAnnotated.with(RestController.class).asClass());

        resources.forEach(re -> System.out.println(Arrays.toString(re.getParameters())));

        System.out.println(singletons.toString());
        Map<String, Object> resp = new HashMap<>();
        resp.put("e", reflections.get(Scanners.TypesAnnotated.with(Entity.class).asClass()));
        resp.put("s", reflections.get(Scanners.TypesAnnotated.with(Service.class).asClass()));
        resp.put("c", reflections.getStore().get("SubTypes").get("org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter"));
        return ResponseEntity.ok(resp);
    }
}
