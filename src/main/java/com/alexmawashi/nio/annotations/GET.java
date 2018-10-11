package com.alexmawashi.nio.annotations;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("GET")
@Documented
public @interface GET {
}