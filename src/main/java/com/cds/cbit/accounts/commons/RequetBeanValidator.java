/*
 * Copyright (C) 2019 Covalensedigital 
 *
 * Licensed under the CBIT,Version 1.0,you may not use this file except in compliance with the 
 * License. You may obtain a copy of the License at 
 * 
 * http://www.covalensedigital.com/
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or
 * implied.See the License for the specific language governing permissions and limitations under.
*/

package com.cds.cbit.accounts.commons;

import com.cds.cbit.accounts.exceptions.ValidationException;

import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The component provides method which validates provided payload with javax validator.
 * 
 * @author  Venkata Nagaraju.
 * @version 1.0.
*/
@Component
public class RequetBeanValidator {
  
  @Autowired
  private Validator validator;

  /** 
   * The method validate given request payload with javax validator to find possible violation.
   * 
   * @param: payload- @Mandatory- Request payload.
   */
  public void validateRequestBean(final Object payload) {

    final Optional<ConstraintViolation<Object>> violation = 
                                             validator.validate(payload).stream().findFirst();
    if (violation.isPresent()) {
      throw new ValidationException(violation.get().getMessage());
    } // End of violation check.
  } // End of validateRequestBean.
} // End of RequetBeanValidator, which validates request bean.