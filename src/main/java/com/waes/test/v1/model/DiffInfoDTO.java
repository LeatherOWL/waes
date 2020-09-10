package com.waes.test.v1.model;

import lombok.Data;

@Data
public class DiffInfoDTO {

    private Long id;
    private boolean areEqual;
    private boolean sizesAreEqual;
    private Diff[] diffs;

}
