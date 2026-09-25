`GET /api/v2/ontologies/{ontology}/objects/{objectType}/{primaryKey}/timeseries/{property}/firstPoint`

Get the first point of a time series property.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |
| `primaryKey` | string | 是 | The primary key of the object with the time series property.<br>示例: `50030` |
| `property` | string | 是 | The API name of the time series property. To find the API name for your time series property,<br>check the **Ontology Manager** or use the **Get object type** endpoint.<br>示例: `performance` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |

## Response

**TimeSeriesPoint**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `TimeSeriesPoint` | object | 是 | Success response. |
| `TimeSeriesPoint.time` | string | 是 | An ISO 8601 timestamp |
| `TimeSeriesPoint.value` | any | 是 | An object which is either an enum String or a double number. |
