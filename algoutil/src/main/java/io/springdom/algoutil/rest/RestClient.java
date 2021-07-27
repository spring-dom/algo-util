package io.springdom.algoutil.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@code RestClient} is a builder type REST API client.
 * JIRA Reference: TMP-5723 - Implementation engineer screen
 *
 * @param <R> Return type.
 * @author <a href="https://stg.opstic.tatacommunications.com/gitlab/Udayabharathi">@Udayabharathi</a>
 * @since <a href="https://jira.vsnl.co.in/browse/TMP-7177">TMP-7177</a>
 */
@SuppressWarnings("WeakerAccess")
@AllArgsConstructor
@Builder(builderMethodName = "restBuilder", buildMethodName = "buildClient")
@Data
@Slf4j
public class RestClient<R> {
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final Object payload;
    private final String uri;
    private final Integer connectTimeOut;
    private final Integer readTimeOut;
    private final Class<R> returnType;

    /**
     * Builder class which will initialize all the attributes!
     *
     * @param <R> Return type.
     */
    @SuppressWarnings("WeakerAccess")
    public static class RestClientBuilder<R> {
        private String baseUrl;

        /**
         * Customized constructor which takes in base URL;
         *
         * @param baseUrl Base URL
         */
        public RestClientBuilder(String baseUrl) {
            this.baseUrl = baseUrl;
            this.uri = baseUrl;
        }

        /**
         * Customised URI builder.
         *
         * @param uriBuilderConsumer Functional interface that takes a lambda as input which takes in a URI Builder
         *                           and returns {@link URI} object.
         * @return {@link RestClientBuilder}&lt;{@code R}&gt;
         */
        public RestClientBuilder<R> uri(Function<UriBuilder, URI> uriBuilderConsumer) {
            UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
            URI uri = uriBuilderConsumer.apply(uriBuilder);
            this.uri = uri.toString();
            return this;
        }

        /**
         * Customized header method that adds a headers.
         *
         * @param key   Header key.
         * @param value Header value.
         * @return {@link RestClientBuilder}&lt;{@code R}&gt;
         */
        public RestClientBuilder<R> header(String key, String value) {
            if (Objects.isNull(this.headers))
                this.headers = new HttpHeaders();
            this.headers.add(key, value);
            return this;
        }

        /**
         * Customized headers builder that takes in a consumer which can be used to add any number of headers.
         *
         * @param httpHeadersBuilder Consumer of {@link HttpHeaders}
         * @return {@link RestClientBuilder}&lt;{@code R}&gt;
         */
        public RestClientBuilder<R> headers(Consumer<HttpHeaders> httpHeadersBuilder) {
            if (Objects.isNull(this.headers))
                this.headers = new HttpHeaders();
            httpHeadersBuilder.accept(this.headers);
            return this;
        }

        /**
         * Retrieves the response by making a REST call.
         *
         * @return {@link ResponseEntity}&lt;{@code R}&gt;
         */
        public ResponseEntity<R> exchange() {
            return buildClient().exchange();
        }

        /**
         * Build method that builds an {@link RestClient} object.
         *
         * @return {@link RestClient}&lt;{@code R}&gt;
         */
        public RestClient<R> buildClient() {
            if (Objects.isNull(this.uri))
                throw new UnsupportedOperationException("URI cannot be null!");
            if (Objects.isNull(this.returnType))
                throw new UnsupportedOperationException("Return type cannot be null!");
            return new RestClient<>(
                    Objects.isNull(this.method) ? HttpMethod.GET : this.method,
                    Objects.isNull(this.headers) ? new HttpHeaders() : this.headers,
                    this.payload,
                    this.uri,
                    Objects.isNull(this.connectTimeOut) ? 60_000 : this.connectTimeOut,
                    Objects.isNull(this.readTimeOut) ? 60_000 : this.readTimeOut,
                    this.returnType
            );
        }
    }

    /**
     * Builder method which takes in a base URL to initialize the builder.
     *
     * @param baseURL Base URL.
     * @param <R>     Return type.
     * @return {@link RestClientBuilder}&lt;{@code R}&gt;
     */
    public static <R> RestClientBuilder<R> restBuilder(String baseURL) {
        return new RestClientBuilder<>(baseURL);
    }

    /**
     * Retrieves the response by making a REST call.
     *
     * @return {@link ResponseEntity}&lt;{@code R}&gt;
     */
    public ResponseEntity<R> exchange() {
        log.info("Retrieve method invoked with {}", this);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(this.connectTimeOut);
        requestFactory.setReadTimeout(this.readTimeOut);
        HttpEntity<Object> request = new HttpEntity<>(this.payload, this.headers);
        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        template.setRequestFactory(requestFactory);
        ResponseEntity<R> response = template.exchange(this.uri, this.method, request, this.returnType);
        log.info("Response status code: {}, Response: {}", response.getStatusCodeValue(), response.getBody());
        return response;
    }
}
