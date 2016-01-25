package us.holdings.common.dao;

import java.io.Serializable;

public interface GenericDrill<T, PK extends Serializable>{

    PK create(T newInstance);

    T read(PK id);

    void update(T transientObject);

    void delete(T persistentObject);
}