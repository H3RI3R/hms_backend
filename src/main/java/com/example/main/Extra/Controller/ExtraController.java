package com.example.main.Extra.Controller;

import com.example.main.Exception.ResponseClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/extra")
@RestController
public class ExtraController {

    @GetMapping("/application")
    public ResponseEntity<?> getApplication(@RequestHeader("Authorization") String token)
    {



        Map<String, String> details = new HashMap<>();
        details.put("ViserHotel_Version", "1.0");
        details.put("Java_Version", "17");
        details.put("Timezone", "UTC");
        details.put("ViserAdmin_Version", "5.0.7");
        return ResponseClass.responseSuccess("data get application","application",details);
//        return ResponseEntity.ok(response);

    }

    @GetMapping("/server")
    public ResponseEntity<?> getServer(@RequestHeader("Authorization") String token)
    {

        Map<String,String> map = new HashMap<>();
        map.put("JAVA_Version","17");
        map.put("Server_Software","AWS");
        map.put("Server_IP_Address","89.116.122.211");
        map.put("Server_Protocol_Version","HTTP/1.1");
        map.put("HTTP_Host","www.auth.edu2all.in");
        map.put("Server_Port","443");
        return ResponseClass.responseSuccess("server detail show","serverDetail",map);
    }

    @GetMapping("/cache")
    public ResponseEntity<?> cache(@RequestHeader("Authorization") String token)
    {
        Map<String,String> strings = new HashMap<>();
        strings.put("cache0","Compiled views will be cleared");
        strings.put("cache1","Application cache will be cleared");
        strings.put("cache2","Route cache will be cleared");
        strings.put("cache3","Configuration cache will be cleared");
        strings.put("cache4","Compiled services and packages files will be removed");
        strings.put("cache5","Caches will be cleared");
        return ResponseClass.responseSuccess("all cache data","cacheData",strings);

    }

    @PostMapping("/clearCache")
    public ResponseEntity<?> clearCache(@RequestHeader("Authorization") String token)
    {
        return ResponseClass.responseSuccess("Cache is cleared");
    }




}
