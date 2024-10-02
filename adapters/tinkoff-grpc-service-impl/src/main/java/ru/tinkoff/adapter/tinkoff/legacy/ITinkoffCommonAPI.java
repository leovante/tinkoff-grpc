package ru.tinkoff.adapter.tinkoff.legacy;

import ru.tinkoff.piapi.core.InvestApi;

public interface ITinkoffCommonAPI {
    InvestApi getApi();

    String getAccountId();

    boolean getIsSandboxMode();
}
