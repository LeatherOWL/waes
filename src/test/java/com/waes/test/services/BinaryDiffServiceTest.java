package com.waes.test.services;

import com.waes.test.repositories.BinaryLeftRepository;
import com.waes.test.repositories.BinaryRightRepository;
import com.waes.test.utils.BinaryConversionUtil;
import com.waes.test.v1.mapper.BinaryMapper;
import com.waes.test.v1.model.BinaryDTO;
import com.waes.test.v1.model.BinaryLeft;
import com.waes.test.v1.model.BinaryRight;
import com.waes.test.v1.model.DiffInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static com.waes.test.utils.BinaryConversionUtil.getBinaryAsBase64ByteArray;
import static com.waes.test.utils.BinaryConversionUtil.getBinaryAsBase64String;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class BinaryDiffServiceTest {

    private BinaryDiffService binaryDiffService;

    @Mock
    BinaryLeftRepository binaryLeftRepository;

    @Mock
    BinaryRightRepository binaryRightRepository;

    private BinaryMapper binaryMapper = BinaryMapper.INSTANCE;

    private BinaryLeft binaryLeft = new BinaryLeft();
    private BinaryRight binaryRight = new BinaryRight();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        binaryDiffService = new BinaryDiffServiceImpl(binaryMapper, binaryLeftRepository, binaryRightRepository);

        binaryLeft.setId(1L);
        binaryLeft.setData(getBinaryAsBase64ByteArray("binaries/nohands.jpg"));

        binaryRight.setId(1L);
        binaryRight.setData(getBinaryAsBase64ByteArray("binaries/nohands.jpg"));
    }

    @Test
    void saveLeftTest() {

        given(binaryLeftRepository.save(any())).willReturn(binaryLeft);

        BinaryDTO binaryDTO = new BinaryDTO();
        binaryDTO.setData(getBinaryAsBase64String("binaries/nohands.jpg"));
        Long id = binaryDiffService.saveLeft(1L, binaryDTO);

        assertThat(id).isEqualTo(1L);
    }

    @Test
    void saveRightTest() {

        given(binaryRightRepository.save(any())).willReturn(binaryRight);

        BinaryDTO binaryDTO = new BinaryDTO();
        binaryDTO.setData(getBinaryAsBase64String("binaries/nohands.jpg"));
        Long id = binaryDiffService.saveRight(1L, binaryDTO);

        assertThat(id).isEqualTo(1L);
    }

    @Test
    void getDiffTest() {

        given(binaryLeftRepository.findById(1L)).willReturn(Optional.of(binaryLeft));
        given(binaryRightRepository.findById(1L)).willReturn(Optional.of(binaryRight));

        DiffInfoDTO diffInfo = binaryDiffService.getDiffInfo(1L);

        assertThat(diffInfo)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("areEqual", true)
                .hasFieldOrPropertyWithValue("sizesAreEqual", true)
                .hasFieldOrPropertyWithValue("diffs", null);
    }

    @Test
    void getDiffNoEqualTest() {

        binaryLeft.setData(getBinaryAsBase64ByteArray("binaries/chris.jpg"));

        given(binaryLeftRepository.findById(1L)).willReturn(Optional.of(binaryLeft));
        given(binaryRightRepository.findById(1L)).willReturn(Optional.of(binaryRight));

        DiffInfoDTO diffInfo = binaryDiffService.getDiffInfo(1L);

        assertThat(diffInfo)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("areEqual", false)
                .hasFieldOrPropertyWithValue("sizesAreEqual", false);

        assertThat(diffInfo.getDiffs()).hasSize(2736);
    }
}