package com.waes.test.v1.mapper;

import com.waes.test.v1.model.BinaryDTO;
import com.waes.test.v1.model.BinaryLeft;
import com.waes.test.v1.model.BinaryRight;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BinaryMapper {

    BinaryMapper INSTANCE = Mappers.getMapper(BinaryMapper.class);

    BinaryLeft binaryDTOToBinaryLeft(BinaryDTO binaryDTO);

    BinaryRight binaryDTOToBinaryRight(BinaryDTO binaryDTO);

    BinaryDTO binaryRightToBinaryDTO(BinaryRight binaryRight);

    BinaryDTO binaryLeftToBinaryDTO(BinaryLeft binaryLeft);

    default byte[] stringToBytes(String data) {
        return data != null ? data.getBytes() : null;
    }

    default String bytesToString(byte[] value) {
        return value != null ? new String(value) : null;
    }
}
