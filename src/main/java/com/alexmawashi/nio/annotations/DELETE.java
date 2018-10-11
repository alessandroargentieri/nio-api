package com.alexmawashi.nio.annotations;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("DELETE")
@Documented
public @interface DELETE {
}