package me.jeff.ignitepoc.model;

import lombok.*;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AlertEntry implements Serializable {

    private Map<String, String> alertContent;

    @QuerySqlField(index = true)
    @NotNull
    private String errorCode;

    @QuerySqlField(index = true)
    @NotNull
    private String serviceId;

    @NotNull
    private String severity;

    @QuerySqlField(index = true)
    private Long timestamp;

    private String alertId;
}
