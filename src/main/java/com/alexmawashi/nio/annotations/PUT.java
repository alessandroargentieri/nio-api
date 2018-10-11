package com.alexmawashi.nio.annotations;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PUT")
@Documented
public @interface PUT {
}