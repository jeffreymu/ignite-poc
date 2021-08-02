package me.jeff.ignitepoc.cache;

import lombok.Data;

import java.io.Serializable;

@Data
public class MoICache implements Cloneable, Serializable {

    private String key;
    private String cacheName;
}

