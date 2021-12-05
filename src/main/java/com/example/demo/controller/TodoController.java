package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    // method create

    @Autowired
    private TodoService todoService;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = todoService.testService();
        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);

    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; // temporary user id.

            // (1) TodoEntity 로 변환한다.
            TodoEntity entity = TodoDTO.todoEntity(dto);

            // (2) id를 null 로 초기화한다. 생성 당시에는 id가 없어야 하기 때문이다.
            entity.setId(null);

            // (3) 임시 사용자 아이디 설정. 인증&인가 적용 시 변경
            entity.setUserId(temporaryUserId);

            // (4) 서비스를 이용해 Todo 엔티티를 생성
            List<TodoEntity> entities = todoService.create(entity);

            // (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (6) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화 한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (7) ResponseDTO 리턴
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            // (8) 혹시 예외가 있는 경우 dto 대신 error 에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user"; // temporary user id.

        // (1) 서비스 메서드의 retrieve() 메서드를 사용해 Todo 리스트를 가져온다.
        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);

        // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // (3) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화 한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);

    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user"; // temporary user id.

        // (1) dto를 entity로 변환한다.
        TodoEntity entity = TodoDTO.todoEntity(dto);

        // (2) id를 temporaryUserId로 초기화
        entity.setUserId(temporaryUserId);

        // (3) 서비스를 이용해 업데이트
        List<TodoEntity> entities = todoService.update(entity);

        // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user"; // temporary user id.

            TodoEntity entity = TodoDTO.todoEntity(dto);

            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
