package org.example.domain.validators;

import org.example.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie>{

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getUser1() == null || entity.getUser2() == null)
            throw new ValidationException("Prietenia nu poate avea un user neexistent");

    }
}
