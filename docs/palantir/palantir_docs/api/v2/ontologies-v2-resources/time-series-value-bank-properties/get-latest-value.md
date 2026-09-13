`GET /api/v2/ontologies/{ontology}/objects/{objectType}/{primaryKey}/timeseries/{propertyName}/latestValue`

Get the latest value of a property backed by a timeseries. If a specific geotime series integration has both a history and a live integration, we will give precedence to the live integration.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |
| `primaryKey` | string | 是 | The primary key of the object with the timeseries property.<br>示例: `50030` |
| `propertyName` | string | 是 | The API name of the timeseries property. To find the API name for your property value bank property,<br>check the **Ontology Manager** or use the **Get object type** endpoint.<br>示例: `performance` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `branch` | string | 否 | The Foundry branch to read from. If not specified, the default branch will be used.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |

## Response

**TimeseriesEntry**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `TimeseriesEntry` | object | 是 | Success response. |
| `TimeseriesEntry.time` | string | 是 | An ISO 8601 timestamp |
| `TimeseriesEntry.value` | any | 是 | An object which is either an enum String, double number, or a geopoint. |
