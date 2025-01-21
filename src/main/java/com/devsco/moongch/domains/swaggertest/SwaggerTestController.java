package com.devsco.moongch.domains.swaggertest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/swagger")
@Tag(name = "Swagger Test", description = "Swagger 테스트")
public class SwaggerTestController {

  @GetMapping
  public ResponseEntity<String> getPosts(
    @PageableDefault(size = 5)
    Pageable pageable) {
    List<Post> posts = List.of(new Post("수학과외 선생 구합니다", "깃이그노어에 스크립트 파일 추가"),
      new Post("수학과외 선생 구합니다", "깃이그노어에 스크립트 파일 추가"),
      new Post("수학과외 선생 구합니다", "깃이그노어에 스크립트 파일 추가"),
      new Post("수학과외 선생 구합니다", "깃이그노어에 스크립트 파일 추가"),
      new Post("수학과외 선생 구합니다", "깃이그노어에 스크립트 파일 추가"));

    return ResponseEntity.ok("Swagger Test");
  }


  public record Post(
    String title,
    String content
  ) {
  }
}
