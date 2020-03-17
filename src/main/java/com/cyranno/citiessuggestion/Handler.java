package com.cyranno.citiessuggestion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cyranno.citiessuggestion.di.AwsLambdaComponent;
import com.cyranno.citiessuggestion.di.DaggerAwsLambdaComponent;
import com.cyranno.citiessuggestion.exception.InvalidParametersException;
import com.cyranno.citiessuggestion.generated.ApiGatewayResponse;
import com.cyranno.citiessuggestion.model.ErrorResponse;
import com.cyranno.citiessuggestion.model.Suggestion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final double AVERAGE = 0.5;
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final String QUERY_HTTP_PARAM = "q";
    private static final String LATITUDE_HTTP_PARAM = "latitude";
    private static final String LONGITUDE_HTTP_PARAM = "longitude";
    private final AwsLambdaComponent awsLambdaComponent;

    public Handler() {
        this.awsLambdaComponent = DaggerAwsLambdaComponent.builder().build();
    }


    @Override
    public ApiGatewayResponse handleRequest(final Map<String, Object> input, final Context context) {
        final Map<String, String> queryStringParameters = (Map<String, String>) input.get("queryStringParameters");
        log.info("input request received: {}", input);
        try {
            this.validateQueryStringParameters(queryStringParameters);
        } catch (final InvalidParametersException e) {
            return ApiGatewayResponse
                    .builder()
                    .setStatusCode(HTTP_BAD_REQUEST)
                    .setObjectBody(new ErrorResponse(e.getMessage()))
                    .build();
        }

        final Set<Suggestion> suggestions = this.awsLambdaComponent.scoringService()
                .calculate(queryStringParameters.get(QUERY_HTTP_PARAM),
                        Double.parseDouble(queryStringParameters.get(LATITUDE_HTTP_PARAM)),
                        Double.parseDouble(queryStringParameters.get(LONGITUDE_HTTP_PARAM)))
                .stream()
                .filter(suggestion -> suggestion.getScore() > AVERAGE)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return ApiGatewayResponse
                .builder()
                .setStatusCode(HTTP_OK)
                .setObjectBody(suggestions)
                .build();
    }

    private void validateQueryStringParameters(final Map<String, String> queryStringParameters) throws InvalidParametersException {
        final String qParameter = queryStringParameters.get("q");
        final String latitudeParameter = queryStringParameters.get("latitude");
        final String longitudeParameter = queryStringParameters.get("longitude");

        if (StringUtils.isEmpty(qParameter)) {
            log.error("qParameter is empty");
            throw new InvalidParametersException("qParameter is empty");
        }

        if (StringUtils.isEmpty(latitudeParameter) || !NumberUtils.isCreatable(latitudeParameter)) {
            log.error("latitudeParameter is empty or invalid");
            throw new InvalidParametersException("latitudeParameter is empty or invalid");
        }

        if (StringUtils.isEmpty(longitudeParameter) || !NumberUtils.isCreatable(longitudeParameter)) {
            log.error("longitudeParameter is empty or invalid");
            throw new InvalidParametersException("longitudeParameter is empty or invalid");
        }
    }
}
