package ru.yandex.api;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpec {

    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";

    private ResponseSpec() {}

    private static final String RESPONSE_CONTENT_TYPE = "application/json";

    private static ResponseSpecBuilder baseSuccess(int status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(RESPONSE_CONTENT_TYPE)
                .expectBody(SUCCESS, equalTo(true));
    }

    private static ResponseSpecBuilder baseError(int status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(RESPONSE_CONTENT_TYPE)
                .expectBody(SUCCESS, equalTo(false));
    }

    public static ResponseSpecification success200() {
        return baseSuccess(SC_OK).build();
    }
}
