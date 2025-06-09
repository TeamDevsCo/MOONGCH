package com.devsco.moongch.presentation;

import com.devsco.moongch.application.CreateMoongchCmd;
import com.devsco.moongch.application.CreateMoongchRes;
import com.devsco.moongch.application.CreateMoongchUseCase;
import com.devsco.moongch.application.CreateTagUseCase;
import com.devsco.moongch.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/moongch")
@RequiredArgsConstructor
public class MoongchController {

  private final CreateTagUseCase createTagUseCase;
  private final CreateMoongchUseCase createMoongchUseCase;

  @PostMapping
  public ResponseEntity<CreateMoongchRes> create(@RequestBody CreateMoongchReq req) {
    try {
      List<Tag> tags = req.tagNames().stream()
        .map(createTagUseCase::createTag)
        .toList();

      CreateMoongchCmd cmd = new CreateMoongchCmd(
        req.userId(),
        req.title(),
        req.description(),
        req.isTemp(),
        req.isOrigin(),
        req.type(),
        req.originMoongchId(),
        tags,
        req.snippets()
      );

      Long id = createMoongchUseCase.create(cmd);

      return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new CreateMoongchRes(id));
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}

