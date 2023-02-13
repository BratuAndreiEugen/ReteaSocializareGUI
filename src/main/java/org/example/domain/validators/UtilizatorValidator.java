package org.example.domain.validators;

import org.example.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator>{

    /**
     *
     * @param entity - ultilizatorul de validat
     * @throws ValidationException daca utilizatorul nu este valid
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getFirstName().length() > 30 || entity.getLastName().length() > 30)
            throw new ValidationException("Numele sau prenumele este prea lung");

    }
}
