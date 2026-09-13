`POST /api/v2/ontologies/{ontology}/objects/{objectType}/search`

Search for objects in the specified ontology and object type. The request body is used
to filter objects based on the specified query. The supported queries are:

| Query type                              | Description                                                                                                       | Supported Types                 |
|-----------------------------------------|-------------------------------------------------------------------------------------------------------------------|---------------------------------|
| lt                                      | The provided property is less than the provided value.                                                            | number, string, date, timestamp |
| gt                                      | The provided property is greater than the provided value.                                                         | number, string, date, timestamp |
| lte                                     | The provided property is less than or equal to the provided value.                                                | number, string, date, timestamp |
| gte                                     | The provided property is greater than or equal to the provided value.                                             | number, string, date, timestamp |
| eq                                      | The provided property is exactly equal to the provided value.                                                     | number, string, date, timestamp |
| isNull                                  | The provided property is (or is not) null.                                                                        | all                             |
| contains                                | The provided property contains the provided value.                                                                | array                           |
| not                                     | The sub-query does not match.                                                                                     | N/A (applied on a query)        |
| and                                     | All the sub-queries match.                                                                                        | N/A (applied on queries)        |
| or                                      | At least one of the sub-queries match.                                                                            | N/A (applied on queries)        |
| containsAllTermsInOrderPrefixLastTerm   | The provided property contains all the terms provided in order. The last term can be a partial prefix match.      | string                          |
| containsAllTermsInOrder                 | The provided property contains the provided term as a substring.                                                  | string                          |
| containsAnyTerm                         | The provided property contains at least one of the terms separated by whitespace.                                 | string                          |
| containsAllTerms                        | The provided property contains all the terms separated by whitespace.                                             | string                          |
| startsWith                              | Deprecated alias for containsAllTermsInOrderPrefixLastTerm.                                                       | string                          |

Queries can be at most three levels deep. By default, terms are separated by whitespace or punctuation (`?!,:;-[](){}'"~`). Periods (`.`) on their own are ignored.
Partial terms are not matched by terms filters except where explicitly noted.

The `pageSize` parameter is a maximum. A page may contain fewer objects than requested if the
objects in the page are large enough to reach an internal memory limit; this does not indicate
that there are no more results. As long as the response contains a `nextPageToken`, the remaining
objects can be retrieved by requesting subsequent pages.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `branch` | string | 否 | The Foundry branch to search objects from. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `executeInMemoryOnly` | boolean | 否 | If true, the request fails with an error when it cannot be computed in-memory.<br>Use this to opt into fast failure on requests that would otherwise require<br>heavier computation.<br>Defaults to false. |

## Request body

```json
{
  "where": {
    "type": "eq",
    "field": "age",
    "value": 21
  }
}
```

## Response

**SearchObjectsResponseV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `SearchObjectsResponseV2` | object | 是 | Success response.<br>示例: `{"data":[{"__rid":"ri.phonograph2-objects.main.object.5b5dbc28-7f05-4e83-a33a-1e5b851ababb","__primaryKey":1000,"__apiName":"Employee","employeeId":1000,"lastName":"smith","firstName":"john","age":21}]}` |
| `SearchObjectsResponseV2.data` | list<OntologyObjectV2> | 否 | — |
| `SearchObjectsResponseV2.data.OntologyObjectV2` | map | 是 | Represents an object in the Ontology. |
| `SearchObjectsResponseV2.data.OntologyObjectV2.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `SearchObjectsResponseV2.data.OntologyObjectV2.PropertyValue` | any | 是 | Represents the value of a property in the following format.<br>\| Type                                                                                                                      \| JSON encoding                                               \| Example                                                                                            \|<br>\|---------------------------------------------------------------------------------------------------------------------------\|-------------------------------------------------------------\|----------------------------------------------------------------------------------------------------\|<br>\| Array                                                                                                                     \| array                                                       \| `["alpha", "bravo", "charlie"]`                                                                    \|<br>\| [Attachment](/docs/foundry/api/v2/ontologies-v2-resources/attachment-properties/attachment-property-basics/)              \| JSON encoded `AttachmentProperty` object                    \| `{"rid":"ri.blobster.main.attachment.2f944bae-5851-4204-8615-920c969a9f2e"}`                       \|<br>\| Boolean                                                                                                                   \| boolean                                                     \| `true`                                                                                             \|<br>\| Byte                                                                                                                      \| number                                                      \| `31`                                                                                               \|<br>\| CipherText                                                                                                                \| string                                                      \| `"CIPHER::ri.bellaso.main.cipher-channel.e414ab9e-b606-499a-a0e1-844fa296ba7e::unzjs3VifsTxuIpf1fH1CJ7OaPBr2bzMMdozPaZJtCii8vVG60yXIEmzoOJaEl9mfFFe::CIPHER"`                                                                                                                                                                                        \|<br>\| Date                                                                                                                      \| ISO 8601 extended local date string                         \| `"2021-05-01"`                                                                                     \|<br>\| Decimal                                                                                                                   \| string                                                      \| `"2.718281828"`                                                                                    \|<br>\| Double                                                                                                                    \| number                                                      \| `3.14159265`                                                                                       \|<br>\| Float                                                                                                                     \| number                                                      \| `3.14159265`                                                                                       \|<br>\| GeoPoint                                                                                                                  \| geojson                                                     \| `{"type":"Point","coordinates":[102.0,0.5]}`                                                       \|<br>\| GeoShape                                                                                                                  \| geojson                                                     \| `{"type":"LineString","coordinates":[[102.0,0.0],[103.0,1.0],[104.0,0.0],[105.0,1.0]]}`            \|<br>\| Integer                                                                                                                   \| number                                                      \| `238940`                                                                                           \|<br>\| Long                                                                                                                      \| string                                                      \| `"58319870951433"`                                                                                 \|<br>\| [MediaReference](/docs/foundry/api/v2/ontologies-v2-resources/media-reference-properties/media-reference-property-basics/)\| JSON encoded `MediaReference` object                        \| `{"mimeType":"application/pdf","reference":{"type":"mediaSetViewItem","mediaSetViewItem":{"mediaSetRid":"ri.mio.main.media-set.4153d42f-ca4b-4e42-8ca5-8e6aa7edb642","mediaSetViewRid":"ri.mio.main.view.82a798ad-d637-4595-acc6-987bcf16629b","mediaItemRid":"ri.mio.main.media-item.001ec98b-1620-4814-9e17-8e9c4e536225"}}}`                       \|<br>\| Secured Property Value                                                                                                    \| JSON encoded `SecuredPropertyValue` object                  \| `{"value": 10, "propertySecurityIndex" : 5}`                                                       \|<br>\| Short                                                                                                                     \| number                                                      \| `8739`                                                                                             \|<br>\| String                                                                                                                    \| string                                                      \| `"Call me Ishmael"`                                                                                \|<br>\| Struct                                                                                                                    \| JSON object of struct field API name -> value               \| {"firstName": "Alex", "lastName": "Karp"}                                                          \|<br>\| Timestamp                                                                                                                 \| ISO 8601 extended offset date-time string in UTC zone       \| `"2021-01-04T05:00:00Z"`                                                                           \|<br>\| [Timeseries](/docs/foundry/api/v2/ontologies-v2-resources/time-series-properties/time-series-property-basics/)            \| JSON encoded `TimeseriesProperty` object or seriesId string \| `{"seriesId": "wellPressureSeriesId", "syncRid": ri.time-series-catalog.main.sync.04f5ac1f-91bf-44f9-a51f-4f34e06e42df"}` or `{"templateRid": "ri.codex-emu.main.template.367cac64-e53b-4653-b111-f61856a63df9", "templateVersion": "0.0.0"}` or `"wellPressureSeriesId"`\|                                                                           \|<br>\| Vector                                                                                                                    \| array                                                       \| `[0.1, 0.3, 0.02, 0.05 , 0.8, 0.4]`                                                                \|<br>Note that for backwards compatibility, the Boolean, Byte, Double, Float, Integer, and Short types can also be encoded as JSON strings. |
| `SearchObjectsResponseV2.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `SearchObjectsResponseV2.totalCount` | string | 是 | The total number of items across all pages.<br>示例: `7587` |

```json
{
  "data": [
    {
      "__rid": "ri.phonograph2-objects.main.object.5b5dbc28-7f05-4e83-a33a-1e5b851ababb",
      "__primaryKey": 1000,
      "__apiName": "Employee",
      "employeeId": 1000,
      "lastName": "smith",
      "firstName": "john",
      "age": 21
    }
  ]
}
```
