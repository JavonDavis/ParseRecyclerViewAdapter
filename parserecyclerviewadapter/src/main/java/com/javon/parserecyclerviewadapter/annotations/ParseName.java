package com.javon.parserecyclerviewadapter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 05/01/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParseName {

    /**
     * @return the desired name of the field on parse
     */
    String value();

}