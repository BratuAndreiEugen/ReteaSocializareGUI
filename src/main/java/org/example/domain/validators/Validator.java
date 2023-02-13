package org.example.domain.validators;

import org.example.domain.Entity;

public interface Validator<E extends Entity> {
    public void validate(E entity) throws ValidationException;
}


