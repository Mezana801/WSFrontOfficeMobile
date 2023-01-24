package vae.vae.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author RKT
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDisable {
    public boolean isDisable() default false;
}
