package com.waes.test.integration;

import com.waes.test.repositories.BinaryLeftRepository;
import com.waes.test.repositories.BinaryRightRepository;
import com.waes.test.v1.model.BinaryDTO;
import com.waes.test.v1.model.DiffInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.waes.test.controllers.BinaryDiffController.BINARY_DIFF_BASE_URL;
import static com.waes.test.utils.BinaryConversionUtil.getBinaryAsBase64String;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BinaryDiffITTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BinaryLeftRepository binaryLeftRepository;

    @Autowired
    private BinaryRightRepository binaryRightRepository;

    @BeforeEach
    void setup() {
        binaryLeftRepository.deleteAll();
        binaryRightRepository.deleteAll();
    }

    @Test
    void getDiffTest() {

        BinaryDTO binaryDTO = new BinaryDTO();
        binaryDTO.setData(getBinaryAsBase64String("binaries/chris.jpg"));

        Long idLeft = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/1/left", binaryDTO, Long.class);
        assertThat(idLeft).isEqualTo(1L);
        Long idRight = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/1/right", binaryDTO, Long.class);
        assertThat(idRight).isEqualTo(1L);

        ResponseEntity<DiffInfoDTO> response = restTemplate.getForEntity(BINARY_DIFF_BASE_URL + "/1", DiffInfoDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("areEqual", true)
                .hasFieldOrPropertyWithValue("sizesAreEqual", true)
                .hasFieldOrPropertyWithValue("diffs", null);
    }

    @Test
    void getDiffNoEqualTest() {

        BinaryDTO binaryDTO = new BinaryDTO();
        binaryDTO.setData(getBinaryAsBase64String("binaries/chris.jpg"));

        Long idLeft = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/1/left", binaryDTO, Long.class);
        assertThat(idLeft).isEqualTo(1L);

        binaryDTO.setData(getBinaryAsBase64String("binaries/chris.jpg")
                .replace("SkZJRgABAQA", "-----------") + "X");

        Long idRight = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/1/right", binaryDTO, Long.class);
        assertThat(idRight).isEqualTo(1L);

        ResponseEntity<DiffInfoDTO> response = restTemplate.getForEntity(BINARY_DIFF_BASE_URL + "/1", DiffInfoDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("areEqual", false)
                .hasFieldOrPropertyWithValue("sizesAreEqual", false);

        assertThat(response.getBody().getDiffs()).hasSize(1);
        assertThat(response.getBody().getDiffs()[0])
                .hasFieldOrPropertyWithValue("offset", 8L)
                .hasFieldOrPropertyWithValue("length", 11L);
    }

    @Test
    void getDiffNoEqualSameSize() {

        BinaryDTO binaryDTO = new BinaryDTO();
        binaryDTO.setData(getBinaryAsBase64String("binaries/chris.jpg"));

        Long idLeft = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/1/left", binaryDTO, Long.class);
        assertThat(idLeft).isEqualTo(1L);

        binaryDTO.setData("X" + getBinaryAsBase64String("binaries/chris.jpg").substring(1));

        Long idRight = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/1/right", binaryDTO, Long.class);
        assertThat(idRight).isEqualTo(1L);

        ResponseEntity<DiffInfoDTO> response = restTemplate.getForEntity(BINARY_DIFF_BASE_URL + "/1", DiffInfoDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("areEqual", false)
                .hasFieldOrPropertyWithValue("sizesAreEqual", true);

        assertThat(response.getBody().getDiffs()).hasSize(1);
        assertThat(response.getBody().getDiffs()[0])
                .hasFieldOrPropertyWithValue("offset", 0L)
                .hasFieldOrPropertyWithValue("length", 1L);

    }

    @Test
    void createBinaryRightTest() {

        BinaryDTO binaryDTO = new BinaryDTO();
        binaryDTO.setData(getBinaryAsBase64String("binaries/crux.jpg"));

        Long createdBinaryId = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/2/right", binaryDTO, Long.class);

        assertThat(createdBinaryId).isEqualTo(2L);
        assertThat(binaryRightRepository.findById(2L)).isNotEmpty();
        assertThat(binaryLeftRepository.findById(2L)).isEmpty();
    }

    @Test
    void binaryNotFoundTest() {

        ResponseEntity<String> response = restTemplate.getForEntity(BINARY_DIFF_BASE_URL + "/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Left Binary with id 1 was not found");
    }

    @Test
    void createBinaryLeftTest() {

        BinaryDTO binaryDTO = new BinaryDTO();
        binaryDTO.setData(getBinaryAsBase64String("binaries/chris.jpg"));

        Long createdBinaryId = restTemplate.postForObject(BINARY_DIFF_BASE_URL + "/1/left", binaryDTO, Long.class);

        assertThat(createdBinaryId).isEqualTo(1L);
        assertThat(binaryLeftRepository.findById(1L)).isNotEmpty();
        assertThat(binaryRightRepository.findById(1L)).isEmpty();
    }
}
