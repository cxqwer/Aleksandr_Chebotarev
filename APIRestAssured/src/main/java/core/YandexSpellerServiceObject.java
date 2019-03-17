package core;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.YandexSpellerConstants.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static constants.YandexSpellerConstants.ParameterName.*;
import static constants.YandexSpellerConstants.SPELLER_URI;
import static constants.YandexSpellerConstants.requestNumber;
import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerServiceObject {

    private Map<String, List<String>> parameters;

    private YandexSpellerServiceObject(Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }

    public static ApiRequestBuilder requestBuilder() {
        return new ApiRequestBuilder();
    }

    public static class ApiRequestBuilder {
        private Map<String, List<String>> parameters = new HashMap<>();

        public ApiRequestBuilder setLanguage(Lang... lang) {
            parameters.put(LANGUAGE.toString(), Arrays.stream(lang).map(l -> l.value).collect(Collectors.toList()));
            return this;
        }

        public ApiRequestBuilder setFormat(Format... format) {
            parameters.put(FORMAT.toString(), Arrays.stream(format).map(f -> f.value).collect(Collectors.toList()));
            return this;
        }

        public ApiRequestBuilder setOptions(Option... options) {
            int resultParameter = 0;
            for (Option o : options) {
                resultParameter += o.value;
            }
            parameters.put(OPTIONS.toString(), Arrays.asList(String.valueOf(resultParameter)));
            return this;
        }

        public ApiRequestBuilder setText(String... text) {
            parameters.put(TEXT.toString(), Arrays.asList(text));
            return this;
        }

        public YandexSpellerServiceObject buildRequest() {
            return new YandexSpellerServiceObject(parameters);
        }
    }

    public Response sendGetRequest() {
        return RestAssured
                .given(requestSpecification())
                .queryParams(parameters)
                .get(SPELLER_URI)
                .prettyPeek();
    }

    public Response sendPostRequest() {
        return RestAssured
                .given(requestSpecification())
                .queryParams(parameters)
                .post(SPELLER_URI)
                .prettyPeek();
    }

    public static List<YandexSpellerAnswer> getAnswers(Response response) {
        List<List<YandexSpellerAnswer>> answers = new Gson()
                .fromJson(response.asString().trim(), new TypeToken<List<List<YandexSpellerAnswer>>>() {
                }.getType());
        return answers.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public static List<String> getStringResult(Response response) {
        return getAnswers(response).stream().map(res -> res.word).collect(Collectors.toList());
    }

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .addQueryParam("requestNumber", ++requestNumber)
                .setBaseUri(SPELLER_URI)
                .build();
    }

    public static ResponseSpecification goodResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(10000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static ResponseSpecification badResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.TEXT)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(10000L))
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .build();
    }
}
