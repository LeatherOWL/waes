package com.waes.test.services;

import com.waes.test.v1.model.BinaryDTO;
import com.waes.test.v1.model.DiffInfoDTO;

public interface BinaryDiffService {

    Long saveLeft(Long id, BinaryDTO binaryDiffDTO);
    Long saveRight(Long id, BinaryDTO binaryDiffDTO);
    DiffInfoDTO getDiffInfo(Long id);
}
