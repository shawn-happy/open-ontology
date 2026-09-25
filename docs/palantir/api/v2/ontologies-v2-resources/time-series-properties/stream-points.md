`POST /api/v2/ontologies/{ontology}/objects/{objectType}/{primaryKey}/timeseries/{property}/streamPoints`

Stream all of the points of a time series property.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |
| `primaryKey` | string | 是 | The primary key of the object with the time series property.<br>示例: `50030` |
| `property` | string | 是 | The API name of the time series property. To find the API name for your time series property,<br>check the **Ontology Manager** or use the **Get object type** endpoint. |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `format` | enum | 否 | The output format to serialize the output binary stream in. Default is<br>JSON. ARROW is more efficient than JSON at streaming a large sized response. |

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
