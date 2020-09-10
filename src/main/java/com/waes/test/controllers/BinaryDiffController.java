package com.waes.test.controllers;

import com.waes.test.services.BinaryDiffService;
import com.waes.test.v1.model.BinaryDTO;
import com.waes.test.v1.model.DiffInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static java.lang.Long.valueOf;

@RestController
@RequestMapping(BinaryDiffController.BINARY_DIFF_BASE_URL)
public class BinaryDiffController {

    public static final String BINARY_DIFF_BASE_URL = "/v1/diff";

    private final BinaryDiffService binaryDiffService;

    public BinaryDiffController(BinaryDiffService binaryDiffService) {
        this.binaryDiffService = binaryDiffService;
    }

    @PostMapping("/{id}/left")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createDataLeft(@PathVariable String id, @RequestBody BinaryDTO binaryDiffDTO) {
        return binaryDiffService.saveLeft(Long.valueOf(id), binaryDiffDTO);
    }

    @PostMapping("/{id}/right")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createDataRight(@PathVariable String id, @RequestBody BinaryDTO binaryDiffDTO) {
        return binaryDiffService.saveRight(Long.valueOf(id), binaryDiffDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DiffInfoDTO getWidgetById(@PathVariable String id) {
        return binaryDiffService.getDiffInfo(valueOf(id));
    }

}
