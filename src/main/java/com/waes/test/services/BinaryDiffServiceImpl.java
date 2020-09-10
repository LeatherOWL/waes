package com.waes.test.services;

import com.waes.test.exceptions.BinaryNotFoundException;
import com.waes.test.repositories.BinaryLeftRepository;
import com.waes.test.repositories.BinaryRightRepository;
import com.waes.test.v1.mapper.BinaryMapper;
import com.waes.test.v1.model.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinaryDiffServiceImpl implements BinaryDiffService {

    private final BinaryMapper binaryMapper;
    private final BinaryLeftRepository binaryLeftRepository;
    private final BinaryRightRepository binaryRightRepository;

    public BinaryDiffServiceImpl(BinaryMapper binaryMapper, BinaryLeftRepository binaryLeftRepository,
                                 BinaryRightRepository binaryRightRepository) {
        this.binaryMapper = binaryMapper;
        this.binaryLeftRepository = binaryLeftRepository;
        this.binaryRightRepository = binaryRightRepository;
    }

    @Override
    public Long saveLeft(Long id, BinaryDTO binaryDiffDTO) {
        BinaryLeft binaryLeft = binaryMapper.binaryDTOToBinaryLeft(binaryDiffDTO);
        binaryLeft.setId(id);
        return binaryLeftRepository.save(binaryLeft).getId();
    }

    @Override
    public Long saveRight(Long id, BinaryDTO binaryDiffDTO) {
        BinaryRight binaryRight = binaryMapper.binaryDTOToBinaryRight(binaryDiffDTO);
        binaryRight.setId(id);
        return binaryRightRepository.save(binaryRight).getId();
    }

    @Override
    public DiffInfoDTO getDiffInfo(Long id) {

        BinaryLeft binaryLeft = binaryLeftRepository.findById(id)
                .orElseThrow(() -> new BinaryNotFoundException("Left Binary with id {} was not found", id));
        BinaryRight binaryRight = binaryRightRepository.findById(id)
                .orElseThrow(() -> new BinaryNotFoundException("Right Binary with id {} was not found", id));

        DiffInfoDTO diffInfoDTO = new DiffInfoDTO();
        diffInfoDTO.setId(id);
        diffInfoDTO.setAreEqual(binaryLeft.equals(binaryRight));
        diffInfoDTO.setSizesAreEqual(binaryLeft.getData().length == binaryRight.getData().length);
        if (!diffInfoDTO.isAreEqual()) {
            diffInfoDTO.setDiffs(getDiffs(binaryLeft.getData(), binaryRight.getData()));
        }
        return diffInfoDTO;
    }

    private Diff[] getDiffs(byte[] leftData, byte[] rightData) {

        List<Diff> result = new ArrayList<>();

        ByteArrayInputStream byteArrayInputStreamLeft = new ByteArrayInputStream(leftData);
        ByteArrayInputStream byteArrayInputStreamRight = new ByteArrayInputStream(rightData);

        int index = 0;
        Diff currentDiff = null;
        while (true) {
            int left = byteArrayInputStreamLeft.read();
            int right = byteArrayInputStreamRight.read();
            if (left == -1 && right == -1) {
                return result.toArray(new Diff[result.size()]); // reached end of both streams
            }
            if (left != right) {
                if (currentDiff == null) {
                    currentDiff = new Diff();
                    currentDiff.setOffset(index);
                    currentDiff.setLength(1);
                } else {
                    currentDiff.setLength(currentDiff.getLength() + 1);
                }
            } else if (currentDiff != null) {
                result.add(currentDiff);
                currentDiff = null;
            }
            index += 1;
        }
    }
}
