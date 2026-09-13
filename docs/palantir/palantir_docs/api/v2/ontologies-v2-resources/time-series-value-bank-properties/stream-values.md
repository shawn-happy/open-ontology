`POST /api/v2/ontologies/{ontology}/objects/{objectType}/{primaryKey}/timeseries/{property}/streamValues`

Stream all of the points of a time series property (this includes geotime series references).


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |
| `primaryKey` | string | 是 | The primary key of the object with the time series property.<br>示例: `50030` |
| `property` | string | 是 | The API name of the time series backed property. To find the API name,<br>check the **Ontology Manager** or use the **Get object type** endpoint. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `branch` | string | 否 | The Foundry branch to read from. If not specified, the default branch will be used.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |

## Request body

```json
{
  "range": {
    "type": "relative",
    "startTime": {
      "when": "BEFORE",
      "value": 5,
      "unit": "MONTHS"
    },
    "endTime": {
      "when": "BEFORE",
      "value": 1,
      "unit": "MONTHS"
    }
  }
}
```

## Response

**body**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | Success response. |
