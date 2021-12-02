package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("/testGetMapping")
    public String testController () {
        return "Hello World";
    }

    @GetMapping("/{id}")
    public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
        return "Hello World ID : " + id;
    }

    @GetMapping("/testRequestParam")
    public String testControllerWithRequestParam(@RequestParam(required = false) int id) {
        return "Hello World ID : " + id;
    }

    @GetMapping("/testRequestBody")
    public String testRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello World ID: " + testRequestBodyDTO.getId() +
                " Message : " + testRequestBodyDTO.getMessage();
    }

    @GetMapping("/testResponse")
    public ResponseDTO<String> testResponse() {
        List<String> list = new ArrayList<>();
        list.add("Hello World test Response");

        return ResponseDTO.<String>builder().data(list).build();
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testResponseEntity() {
        List<String> list = new ArrayList<>();
        list.add("Hello World test ResponseEntity");

        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.badRequest().body(response);
        //return ResponseEntity.ok().body(response);
    }

}
