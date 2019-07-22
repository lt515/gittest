package cn.edu.zsc.rms.api.rest;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;

import static capital.scalable.restdocs.SnippetRegistry.*;
import static capital.scalable.restdocs.response.ResponseModifyingPreprocessors.limitJsonArrayLength;
import static capital.scalable.restdocs.response.ResponseModifyingPreprocessors.replaceBinaryContent;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * @author hsj
 */
@WebMvcTest
public class RestAutoDocConfig implements RestDocsMockMvcConfigurationCustomizer,
        MockMvcBuilderCustomizer {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void customize(MockMvcRestDocumentationConfigurer configurer) {
        configurer.operationPreprocessors()
                .withResponseDefaults(replaceBinaryContent(),
                        limitJsonArrayLength(objectMapper),
                        prettyPrint())
                .and()
                .snippets().withDefaults(
                AutoDocumentation.methodAndPath(),
                AutoDocumentation.description(),
                CliDocumentation.httpieRequest(),
                PayloadDocumentation.responseBody(),
                AutoDocumentation.pathParameters(),
                AutoDocumentation.requestParameters(),
                AutoDocumentation.requestFields(),
                AutoDocumentation.responseFields(),
                AutoDocumentation.sectionBuilder()
                .snippetNames(
                        HTTPIE_REQUEST,
                        RESPONSE_BODY,
                        AUTO_PATH_PARAMETERS,
                        AUTO_REQUEST_PARAMETERS,
                        AUTO_REQUEST_FIELDS,
                        AUTO_RESPONSE_FIELDS
                ).skipEmpty(true).build()
        );

    }

    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
       builder.alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper));
    }
}
