package me.jeff.ignitepoc.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by romeh on 09/08/2017.
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
public class AlertHolder implements Serializable {
    private String serviceCode;
    private List<AlertEntry> alerts;
}
