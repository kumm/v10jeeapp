package com.acme.v10jeeapp.backend;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity implements Serializable {

    abstract protected Object getId();

    @Override
    public  boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity entity = (AbstractEntity) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
