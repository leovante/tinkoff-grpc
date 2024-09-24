package ru.tinkoff.adapter.service.moexiss;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.adapter.client.moexIss.api.IssApi;
import ru.tinkoff.adapter.client.moexIss.api.SpecificationsApiClient;
import ru.tinkoff.adapter.client.moexIss.payload.RSecuritiesSpec;
import ru.tinkoff.adapter.client.moexIss.payload.Row;
import ru.tinkoff.adapter.dto.RequestParamSecuritiesCandles;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MoexIssService {

    private final SpecificationsApiClient specificationsApiClient;
    private final IssApi issApi;

    public List<String> fetchIsinList(Integer listing) {
        try {
            return specificationsApiClient.getSecuritiesSpecificationsSync(listing).getBody().stream()
                    .map(RSecuritiesSpec::getIsin)
                    .toList();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Row> fetchCandles(RequestParamSecuritiesCandles request) {
        try {
            return issApi.getSecuritiesCandlesSync(
                            request.getSecurity(),
                            request.getFrom(),
                            request.getInterval(),
                            null,
                            null).getBody().stream()
                    .toList();
        } catch (Exception e) {
            throw e;
        }
    }

}
