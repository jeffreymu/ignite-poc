package me.jeff.ignitepoc.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class MoIResponse implements Serializable {

    private static final long serialVersionUID = -7755563009111273632L;

    private String errorCode;

    private String errorMessage;
}
